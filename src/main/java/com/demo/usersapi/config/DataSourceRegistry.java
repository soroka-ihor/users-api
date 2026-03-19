package com.demo.usersapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

@Configuration
@RequiredArgsConstructor
public class DataSourceRegistry {

    private final DataSourceProperties dataSourceProperties;

    @Bean
    public Executor taskExecutor() {
        DataSourceProperties.AsyncConfig async = dataSourceProperties.getAsync();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(async.getCorePoolSize());
        executor.setMaxPoolSize(async.getMaxPoolSize());
        executor.setQueueCapacity(async.getQueueCapacity());
        executor.setKeepAliveSeconds(async.getKeepAliveSeconds());
        executor.setThreadNamePrefix("db-query-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    @Bean
    public Map<String, DataSource> dataSources() {
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        for (DataSourceConfig config : dataSourceProperties.getDataSources()) {
            DataSourceConfig.PoolConfig pool = config.getPool();

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(config.getUrl());
            hikariConfig.setUsername(config.getUser());
            hikariConfig.setPassword(config.getPassword());
            hikariConfig.setPoolName("Pool-" + config.getName());
            hikariConfig.setMaximumPoolSize(pool.getMaximumPoolSize());
            hikariConfig.setMinimumIdle(pool.getMinimumIdle());
            hikariConfig.setConnectionTimeout(pool.getConnectionTimeoutMs());
            hikariConfig.setIdleTimeout(pool.getIdleTimeoutMs());
            hikariConfig.setMaxLifetime(pool.getMaxLifetimeMs());

            dataSourceMap.put(config.getName(), new HikariDataSource(hikariConfig));
        }
        return dataSourceMap;
    }
}
