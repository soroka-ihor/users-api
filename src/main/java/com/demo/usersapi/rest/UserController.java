package com.demo.usersapi.rest;

import com.demo.usersapi.model.UserFilter;
import com.demo.usersapi.service.UserAggregationService;
import com.example.aggregator.api.UsersApi;
import com.example.aggregator.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UserAggregationService aggregationServiceFacade;

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers(String name, String username, String surname) {
        UserFilter filter = new UserFilter(name, username, surname);
        return ResponseEntity.ok(aggregationServiceFacade.fetchAllUsers(filter));
    }
}
