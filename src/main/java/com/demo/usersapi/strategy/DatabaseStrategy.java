package com.demo.usersapi.strategy;

import com.demo.usersapi.config.MappingConfig;
import com.demo.usersapi.model.QuerySpec;
import com.demo.usersapi.model.UserFilter;

public interface DatabaseStrategy {
    QuerySpec buildQuery(String tableName, MappingConfig mappingConfig, UserFilter filter);
    String getStrategyName();
}
