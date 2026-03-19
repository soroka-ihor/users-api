package com.demo.usersapi.strategy;

import com.demo.usersapi.config.MappingConfig;
import com.demo.usersapi.model.QuerySpec;
import com.demo.usersapi.model.UserFilter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PostgresStrategy extends DefaultDatabaseStrategy {

    @Override
    public QuerySpec buildQuery(String table, MappingConfig mapping, UserFilter filter) {
        String baseSql = String.format("SELECT %s AS id, %s AS username, %s AS name, %s AS surname FROM %s",
                mapping.getId(), mapping.getUsername(), mapping.getName(), mapping.getSurname(), table);
        StringBuilder sqlBuilder = new StringBuilder(baseSql);
        Map<String, Object> params = new HashMap<>();
        appendWhereClause(sqlBuilder, mapping, filter, params);
        return new QuerySpec(sqlBuilder.toString(), params);
    }

    @Override
    public String getStrategyName() {
        return "postgres";
    }
}
