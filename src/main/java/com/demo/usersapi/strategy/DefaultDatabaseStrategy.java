package com.demo.usersapi.strategy;

import com.demo.usersapi.config.MappingConfig;
import com.demo.usersapi.model.QuerySpec;
import com.demo.usersapi.model.UserFilter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DefaultDatabaseStrategy implements DatabaseStrategy {

    @Override
    public QuerySpec buildQuery(String tableName, MappingConfig mappingConfig, UserFilter filter) {
        String columns = mappingConfig.toMap().entrySet().stream()
                .map(e -> e.getValue() + " AS " + e.getKey())
                .collect(Collectors.joining(", "));
        StringBuilder sql = new StringBuilder("SELECT ").append(columns).append(" FROM ").append(tableName);
        Map<String, Object> params = new HashMap<>();
        appendWhereClause(sql, mappingConfig, filter, params);
        return new QuerySpec(sql.toString(), params);
    }

    protected void appendWhereClause(StringBuilder sql, MappingConfig mapping, UserFilter filter, Map<String, Object> params) {
        List<String> conditions = new ArrayList<>();
        if (filter.name() != null) {
            conditions.add(mapping.getName() + " LIKE :name");
            params.put("name", "%" + filter.name() + "%");
        }
        if (filter.username() != null) {
            conditions.add(mapping.getUsername() + " LIKE :username");
            params.put("username", "%" + filter.username() + "%");
        }
        if (filter.surname() != null) {
            conditions.add(mapping.getSurname() + " LIKE :surname");
            params.put("surname", "%" + filter.surname() + "%");
        }
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }
    }

    @Override
    public String getStrategyName() {
        return "default";
    }
}
