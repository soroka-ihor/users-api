package com.demo.usersapi.repository;

import com.demo.usersapi.config.DataSourceConfig;
import com.demo.usersapi.model.User;
import com.demo.usersapi.model.UserFilter;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AsyncResilientUserRepository implements UserRepository {

    private final Executor taskExecutor;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    protected AsyncResilientUserRepository(Executor taskExecutor,
                                           CircuitBreakerRegistry circuitBreakerRegistry) {
        this.taskExecutor = taskExecutor;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Override
    public CompletableFuture<List<User>> findAllUsersAsync(DataSourceConfig config, UserFilter filter) {
        CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker(config.getName());
        return CompletableFuture
                .supplyAsync(CircuitBreaker.decorateSupplier(cb, () -> {
                    log.info("Fetching users from [{}] at URL: {}", config.getName(), config.getUrl());
                    return findAllUsers(config, filter);
                }), taskExecutor)
                .orTimeout(config.getQueryTimeoutSeconds(), TimeUnit.SECONDS)
                .handle((result, ex) -> {
                    if (Objects.nonNull(ex)) {
                        log.error("Error fetching from [{}]: {}", config.getName(), ex.getMessage());
                        return Collections.emptyList();
                    }
                    log.info("Retrieved {} users from [{}]", result.size(), config.getName());
                    return result;
                });
    }
}
