package com.demo.usersapi.strategy;

import com.demo.usersapi.config.MappingConfig;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DefaultDatabaseStrategy implements DatabaseStrategy {

    @Override
    public String buildQuery(String tableName, MappingConfig mappingConfig) {
        String columns = mappingConfig.toMap().entrySet().stream()
                .map(e -> e.getValue() + " AS " + e.getKey())
                .collect(Collectors.joining(", "));
        return "SELECT " + columns + " FROM " + tableName;
    }

    @Override
    public String getStrategyName() {
        return "default";
    }
}
