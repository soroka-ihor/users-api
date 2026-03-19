package com.demo.usersapi.factory;

import com.demo.usersapi.strategy.DatabaseStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DefaultDatabaseStrategyRegistry implements DatabaseStrategyRegistry {

    private final List<DatabaseStrategy> databaseStrategies;

    @Override
    public Map<String, DatabaseStrategy> getDatabaseStrategies() {
        return databaseStrategies.stream()
                .collect(Collectors.toMap(
                        strategy -> strategy.getStrategyName().toLowerCase(),
                        strategy -> strategy
                ));
    }
}
