package com.demo.usersapi.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DataSourceConfig {

    @NotBlank
    private String name;

    @NotBlank
    private String strategy;

    @NotBlank
    private String url;

    @NotBlank
    private String table;

    @NotBlank
    private String user;

    @NotBlank
    private String password;

    @NotNull
    @Valid
    private MappingConfig mapping;

    @Positive
    private int queryTimeoutSeconds = 5;

    @NotNull
    private PoolConfig pool = new PoolConfig();

    @Data
    public static class PoolConfig {
        private int maximumPoolSize = 10;
        private int minimumIdle = 2;
        private long connectionTimeoutMs = 30000;
        private long idleTimeoutMs = 600000;
        private long maxLifetimeMs = 1800000;
    }
}
