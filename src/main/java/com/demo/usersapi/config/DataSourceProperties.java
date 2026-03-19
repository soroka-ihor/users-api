package com.demo.usersapi.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "app")
public class DataSourceProperties {

    @NotEmpty
    @Valid
    private List<DataSourceConfig> dataSources;

    @Valid
    private AsyncConfig async = new AsyncConfig();

    @Data
    public static class AsyncConfig {
        @Positive private int corePoolSize = 5;
        @Positive private int maxPoolSize = 20;
        @Positive private int queueCapacity = 100;
        @Positive private int keepAliveSeconds = 60;
    }
}
