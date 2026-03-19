package com.demo.usersapi.service;

import com.demo.usersapi.config.DataSourceConfig;
import com.demo.usersapi.config.DataSourceProperties;
import com.demo.usersapi.mapper.UserMapper;
import com.demo.usersapi.model.User;
import com.demo.usersapi.repository.UserRepository;
import com.example.aggregator.model.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserAggregationServiceTest {

    private UserMapper userMapper;
    private UserRepository userRepository;
    private DataSourceProperties dataSourceProperties;
    private UserAggregationService service;

    @BeforeEach
    void setUp() {
        userMapper = mock(UserMapper.class);
        userRepository = mock(UserRepository.class);
        dataSourceProperties = mock(DataSourceProperties.class);
        service = new UserAggregationService(userMapper, userRepository, dataSourceProperties);
    }

    @Test
    void fetchAllUsers_returnsMappedUsersFromAllSources() {
        DataSourceConfig config1 = configWithName("db1");
        DataSourceConfig config2 = configWithName("db2");

        User user1 = new User("1", "jdoe", "John", "Doe");
        User user2 = new User("2", "jsmith", "Jane", "Smith");

        UserDto dto1 = new UserDto("1", "jdoe", "John", "Doe");
        UserDto dto2 = new UserDto("2", "jsmith", "Jane", "Smith");

        when(dataSourceProperties.getDataSources()).thenReturn(List.of(config1, config2));
        when(userRepository.findAllUsersAsync(config1)).thenReturn(CompletableFuture.completedFuture(List.of(user1)));
        when(userRepository.findAllUsersAsync(config2)).thenReturn(CompletableFuture.completedFuture(List.of(user2)));
        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        List<UserDto> result = service.fetchAllUsers();

        assertThat(result).containsExactlyInAnyOrder(dto1, dto2);
    }

    private DataSourceConfig configWithName(String name) {
        DataSourceConfig config = new DataSourceConfig();
        config.setName(name);
        config.setStrategy("postgres");
        config.setUrl("jdbc:h2:mem:" + name);
        config.setTable("users");
        config.setUser("sa");
        config.setPassword("");
        config.setMapping(new com.demo.usersapi.config.MappingConfig());
        return config;
    }

    @Test
    void fetchAllUsers_returnsEmptyList_whenNoDataSourcesConfigured() {
        when(dataSourceProperties.getDataSources()).thenReturn(List.of());

        List<UserDto> result = service.fetchAllUsers();

        assertThat(result).isEmpty();
        verifyNoInteractions(userRepository, userMapper);
    }

    @Test
    void fetchAllUsers_returnsEmptyList_whenAllSourcesReturnNoUsers() {
        DataSourceConfig config = configWithName("db1");
        when(dataSourceProperties.getDataSources()).thenReturn(List.of(config));
        when(userRepository.findAllUsersAsync(config)).thenReturn(CompletableFuture.completedFuture(List.of()));

        List<UserDto> result = service.fetchAllUsers();

        assertThat(result).isEmpty();
        verifyNoInteractions(userMapper);
    }

    @Test
    void fetchAllUsers_flattensUsersAcrossMultipleSources() {
        DataSourceConfig config1 = configWithName("db1");
        DataSourceConfig config2 = configWithName("db2");

        User user1 = new User("1", "jdoe", "John", "Doe");
        User user2 = new User("2", "jsmith", "Jane", "Smith");
        User user3 = new User("3", "bwillis", "Bruce", "Willis");

        UserDto dto1 = new UserDto("1", "jdoe", "John", "Doe");
        UserDto dto2 = new UserDto("2", "jsmith", "Jane", "Smith");
        UserDto dto3 = new UserDto("3", "bwillis", "Bruce", "Willis");

        when(dataSourceProperties.getDataSources()).thenReturn(List.of(config1, config2));
        when(userRepository.findAllUsersAsync(config1)).thenReturn(CompletableFuture.completedFuture(List.of(user1, user2)));
        when(userRepository.findAllUsersAsync(config2)).thenReturn(CompletableFuture.completedFuture(List.of(user3)));
        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);
        when(userMapper.toDto(user3)).thenReturn(dto3);

        List<UserDto> result = service.fetchAllUsers();

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrder(dto1, dto2, dto3);
    }

    @Test
    void fetchAllUsers_mapsEveryUserThroughMapper() {
        DataSourceConfig config = configWithName("db1");
        User user1 = new User("1", "jdoe", "John", "Doe");
        User user2 = new User("2", "jsmith", "Jane", "Smith");

        when(dataSourceProperties.getDataSources()).thenReturn(List.of(config));
        when(userRepository.findAllUsersAsync(config)).thenReturn(CompletableFuture.completedFuture(List.of(user1, user2)));
        when(userMapper.toDto(any())).thenReturn(new UserDto("x", "x", "x", "x"));

        service.fetchAllUsers();

        verify(userMapper).toDto(user1);
        verify(userMapper).toDto(user2);
        verifyNoMoreInteractions(userMapper);
    }
}
