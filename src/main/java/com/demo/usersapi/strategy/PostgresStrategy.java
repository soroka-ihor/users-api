package com.demo.usersapi.strategy;

import com.demo.usersapi.config.MappingConfig;
import org.springframework.stereotype.Component;

@Component
public class PostgresStrategy extends DefaultDatabaseStrategy {

    @Override
    public String buildQuery(String table, MappingConfig mapping) {
        return String.format("SELECT %s AS id, %s AS username, %s AS name, %s AS surname FROM %s",
                mapping.getId(), mapping.getUsername(), mapping.getName(), mapping.getSurname(), table);
    }

    @Override
    public String getStrategyName() {
        return "postgres";
    }
}
