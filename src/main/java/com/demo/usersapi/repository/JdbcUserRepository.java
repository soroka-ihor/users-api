package com.demo.usersapi.repository;

import com.demo.usersapi.config.DataSourceConfig;
import com.demo.usersapi.factory.DatabaseStrategyRegistry;
import com.demo.usersapi.model.QuerySpec;
import com.demo.usersapi.model.User;
import com.demo.usersapi.model.UserFilter;
import com.demo.usersapi.strategy.DatabaseStrategy;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Repository
public class JdbcUserRepository extends AsyncResilientUserRepository {

    private final Map<String, DataSource> dataSources;
    private final DatabaseStrategyRegistry databaseStrategyRegistry;

    public JdbcUserRepository(Map<String, DataSource> dataSources,
                               DatabaseStrategyRegistry databaseStrategyRegistry,
                               @Qualifier("taskExecutor") Executor taskExecutor,
                               CircuitBreakerRegistry circuitBreakerRegistry) {
        super(taskExecutor, circuitBreakerRegistry);
        this.dataSources = dataSources;
        this.databaseStrategyRegistry = databaseStrategyRegistry;
    }

    @Override
    public List<User> findAllUsers(DataSourceConfig config, UserFilter filter) {
        DataSource dataSource = dataSources.get(config.getName());
        if (dataSource == null) {
            throw new IllegalStateException("No DataSource bean found for name: %s".formatted(config.getName()));
        }

        Map<String, DatabaseStrategy> strategies = databaseStrategyRegistry.getDatabaseStrategies();
        DatabaseStrategy strategy = strategies.getOrDefault(
                config.getStrategy().toLowerCase(),
                strategies.get("default")
        );

        QuerySpec querySpec = strategy.buildQuery(config.getTable(), config.getMapping(), filter);
        return new NamedParameterJdbcTemplate(dataSource)
                .query(querySpec.sql(), querySpec.params(), new DataClassRowMapper<>(User.class));
    }
}
