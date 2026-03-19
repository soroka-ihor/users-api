package com.demo.usersapi.repository;

import com.demo.usersapi.config.DataSourceConfig;
import com.demo.usersapi.model.User;
import com.demo.usersapi.model.UserFilter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserRepository {
    List<User> findAllUsers(DataSourceConfig config, UserFilter filter);
    CompletableFuture<List<User>> findAllUsersAsync(DataSourceConfig config, UserFilter filter);
}
