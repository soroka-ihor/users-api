package com.demo.usersapi.factory;

import com.demo.usersapi.strategy.DatabaseStrategy;

import java.util.Map;

public interface DatabaseStrategyRegistry {
    Map<String, DatabaseStrategy> getDatabaseStrategies();
}
