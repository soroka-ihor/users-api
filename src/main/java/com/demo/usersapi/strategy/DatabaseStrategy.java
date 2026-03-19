package com.demo.usersapi.strategy;

import com.demo.usersapi.config.MappingConfig;

public interface DatabaseStrategy {
    String buildQuery(String tableName, MappingConfig mappingConfig);
    String getStrategyName();
}
