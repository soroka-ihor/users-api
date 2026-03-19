package com.demo.usersapi.rest;

import com.demo.usersapi.model.UserFilter;
import com.demo.usersapi.service.UserAggregationService;
import com.example.aggregator.model.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private final UserAggregationService aggregationService = mock(UserAggregationService.class);
    private final UserController controller = new UserController(aggregationService);

    @Test
    void getAllUsers_returnsOkWithUserList() {
        List<UserDto> users = List.of(
                new UserDto("1", "jdoe", "John", "Doe"),
                new UserDto("2", "jsmith", "Jane", "Smith")
        );
        when(aggregationService.fetchAllUsers(any())).thenReturn(users);

        ResponseEntity<List<UserDto>> response = controller.getAllUsers(null, null, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(users);
    }

    @Test
    void getAllUsers_returnsOkWithEmptyList_whenNoUsersFound() {
        when(aggregationService.fetchAllUsers(any())).thenReturn(List.of());

        ResponseEntity<List<UserDto>> response = controller.getAllUsers(null, null, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void getAllUsers_delegatesToAggregationService() {
        when(aggregationService.fetchAllUsers(any())).thenReturn(List.of());

        controller.getAllUsers(null, null, null);

        verify(aggregationService).fetchAllUsers(any(UserFilter.class));
        verifyNoMoreInteractions(aggregationService);
    }

    @Test
    void getAllUsers_passesFilterParamsToService() {
        when(aggregationService.fetchAllUsers(any())).thenReturn(List.of());

        controller.getAllUsers("John", "jdoe", "Doe");

        verify(aggregationService).fetchAllUsers(new UserFilter("John", "jdoe", "Doe"));
    }
}
