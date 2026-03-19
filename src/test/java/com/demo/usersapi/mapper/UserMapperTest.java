package com.demo.usersapi.mapper;

import com.demo.usersapi.model.User;
import com.example.aggregator.model.UserDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toDto_mapsAllFields() {
        User user = new User("u1", "jdoe", "John", "Doe");

        UserDto dto = mapper.toDto(user);

        assertThat(dto.getId()).isEqualTo("u1");
        assertThat(dto.getUsername()).isEqualTo("jdoe");
        assertThat(dto.getName()).isEqualTo("John");
        assertThat(dto.getSurname()).isEqualTo("Doe");
    }

    @Test
    void toDto_returnsNull_whenInputIsNull() {
        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    void toDto_mapsNullFieldsAsNull() {
        User user = new User(null, null, null, null);

        UserDto dto = mapper.toDto(user);

        assertThat(dto.getId()).isNull();
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getName()).isNull();
        assertThat(dto.getSurname()).isNull();
    }
}
