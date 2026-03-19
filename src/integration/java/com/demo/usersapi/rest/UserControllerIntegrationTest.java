package com.demo.usersapi.rest;

import com.demo.usersapi.BaseAbstractIntegrationTest;
import com.example.aggregator.model.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerIntegrationTest extends BaseAbstractIntegrationTest {

    @Test
    void getAllUsers_returnsOkWithUsersFromBothDatabases() {
        ResponseEntity<List<UserDto>> response = restTemplate.exchange(
                "/users", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .hasSize(10);
    }

    @Test
    void getAllUsers_allUsersHaveRequiredFields() {
        ResponseEntity<List<UserDto>> response = restTemplate.exchange(
                "/users", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertThat(response.getBody()).allSatisfy(user -> {
            assertThat(user.getId()).isNotBlank();
            assertThat(user.getUsername()).isNotBlank();
            assertThat(user.getName()).isNotBlank();
            assertThat(user.getSurname()).isNotBlank();
        });
    }

    @Test
    void getAllUsers_containsExpectedPostgresUsers() {
        ResponseEntity<List<UserDto>> response = restTemplate.exchange(
                "/users", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertThat(response.getBody())
                .extracting(UserDto::getId)
                .contains("p-001", "p-002", "p-003", "p-004", "p-005");
    }

    @Test
    void getAllUsers_containsExpectedOracleUsers() {
        ResponseEntity<List<UserDto>> response = restTemplate.exchange(
                "/users", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertThat(response.getBody())
                .extracting(UserDto::getId)
                .contains("o-001", "o-002", "o-003", "o-004", "o-005");
    }
}
