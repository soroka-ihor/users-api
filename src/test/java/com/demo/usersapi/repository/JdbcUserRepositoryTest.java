package com.demo.usersapi.repository;

import com.demo.usersapi.config.DataSourceConfig;
import com.demo.usersapi.config.MappingConfig;
import com.demo.usersapi.factory.DatabaseStrategyRegistry;
import com.demo.usersapi.model.QuerySpec;
import com.demo.usersapi.model.User;
import com.demo.usersapi.model.UserFilter;
import com.demo.usersapi.strategy.DatabaseStrategy;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JdbcUserRepositoryTest {

    private static final QuerySpec QUERY = new QuerySpec("SELECT id, username, name, surname FROM users", Map.of());
    private static final QuerySpec EMPTY_QUERY = new QuerySpec("SELECT id, username, name, surname FROM empty_users", Map.of());

    private EmbeddedDatabase dataSource;
    private DatabaseStrategy strategy;
    private DatabaseStrategyRegistry strategyRegistry;
    private JdbcUserRepository repository;
    private DataSourceConfig config;

    @AfterEach
    void tearDown() {
        dataSource.shutdown();
    }

    @BeforeEach
    void setUp() {
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .generateUniqueName(true)
                .addScript("classpath:schema-repo.sql")
                .addScript("classpath:data-repo.sql")
                .build();

        strategy = mock(DatabaseStrategy.class);
        strategyRegistry = mock(DatabaseStrategyRegistry.class);
        when(strategyRegistry.getDatabaseStrategies()).thenReturn(Map.of("postgres", strategy));

        repository = new JdbcUserRepository(
                Map.of("test-db", dataSource),
                strategyRegistry,
                mock(Executor.class),
                CircuitBreakerRegistry.ofDefaults()
        );

        MappingConfig mapping = new MappingConfig();
        mapping.setId("id");
        mapping.setUsername("username");
        mapping.setName("name");
        mapping.setSurname("surname");

        config = new DataSourceConfig();
        config.setName("test-db");
        config.setStrategy("postgres");
        config.setTable("users");
        config.setUrl("jdbc:h2:mem:test");
        config.setUser("sa");
        config.setPassword("");
        config.setMapping(mapping);
    }

    @Test
    void findAllUsers_returnsAllUsersFromDataSource() {
        when(strategy.buildQuery(any(), any(), any())).thenReturn(QUERY);

        List<User> result = repository.findAllUsers(config, UserFilter.empty());

        assertThat(result).hasSize(2);
        assertThat(result).extracting(User::getId).containsExactlyInAnyOrder("1", "2");
        assertThat(result).extracting(User::getUsername).containsExactlyInAnyOrder("jdoe", "jsmith");
    }

    @Test
    void findAllUsers_throwsIllegalStateException_whenDataSourceNotFound() {
        config.setName("unknown-db");

        assertThatThrownBy(() -> repository.findAllUsers(config, UserFilter.empty()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unknown-db");
    }

    @Test
    void findAllUsers_usesDefaultStrategy_whenConfiguredStrategyNotRegistered() {
        DatabaseStrategy defaultStrategy = mock(DatabaseStrategy.class);
        when(defaultStrategy.buildQuery(any(), any(), any())).thenReturn(QUERY);
        when(strategyRegistry.getDatabaseStrategies()).thenReturn(Map.of("default", defaultStrategy));

        config.setStrategy("unknown-strategy");

        List<User> result = repository.findAllUsers(config, UserFilter.empty());

        assertThat(result).hasSize(2);
        verify(defaultStrategy).buildQuery(eq("users"), any(), any());
    }

    @Test
    void findAllUsers_lowercasesStrategyName_beforeLookup() {
        when(strategy.buildQuery(any(), any(), any())).thenReturn(QUERY);
        config.setStrategy("POSTGRES");

        List<User> result = repository.findAllUsers(config, UserFilter.empty());

        assertThat(result).hasSize(2);
        verify(strategy).buildQuery(any(), any(), any());
    }

    @Test
    void findAllUsers_passesTableAndMappingToStrategy() {
        when(strategy.buildQuery(any(), any(), any())).thenReturn(QUERY);

        repository.findAllUsers(config, UserFilter.empty());

        verify(strategy).buildQuery(eq("users"), eq(config.getMapping()), any());
    }

    @Test
    void findAllUsers_returnsEmptyList_whenTableHasNoRows() {
        when(strategy.buildQuery(any(), any(), any())).thenReturn(EMPTY_QUERY);

        List<User> result = repository.findAllUsers(config, UserFilter.empty());

        assertThat(result).isEmpty();
    }
}
