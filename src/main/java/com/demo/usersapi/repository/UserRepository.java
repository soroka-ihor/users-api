package com.demo.usersapi.repository;

import com.demo.usersapi.config.DataSourceConfig;
import com.demo.usersapi.model.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserRepository {
    List<User> findAllUsers(DataSourceConfig config);
    CompletableFuture<List<User>> findAllUsersAsync(DataSourceConfig config);
}
