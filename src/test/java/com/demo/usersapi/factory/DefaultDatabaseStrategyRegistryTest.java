package com.demo.usersapi.factory;

import com.demo.usersapi.strategy.DatabaseStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultDatabaseStrategyRegistryTest {

    @Test
    void getDatabaseStrategies_returnsEmptyMap_whenNoStrategiesRegistered() {
        DefaultDatabaseStrategyRegistry registry = new DefaultDatabaseStrategyRegistry(List.of());

        Map<String, DatabaseStrategy> result = registry.getDatabaseStrategies();

        assertThat(result).isEmpty();
    }

    @Test
    void getDatabaseStrategies_returnsSingleEntry_forOneStrategy() {
        DatabaseStrategy strategy = mock(DatabaseStrategy.class);
        when(strategy.getStrategyName()).thenReturn("postgres");

        DefaultDatabaseStrategyRegistry registry = new DefaultDatabaseStrategyRegistry(List.of(strategy));

        Map<String, DatabaseStrategy> result = registry.getDatabaseStrategies();

        assertThat(result).containsOnlyKeys("postgres");
        assertThat(result.get("postgres")).isSameAs(strategy);
    }

    @Test
    void getDatabaseStrategies_returnsAllEntries_forMultipleStrategies() {
        DatabaseStrategy postgres = mock(DatabaseStrategy.class);
        when(postgres.getStrategyName()).thenReturn("postgres");

        DatabaseStrategy oracle = mock(DatabaseStrategy.class);
        when(oracle.getStrategyName()).thenReturn("oracle");

        DefaultDatabaseStrategyRegistry registry = new DefaultDatabaseStrategyRegistry(List.of(postgres, oracle));

        Map<String, DatabaseStrategy> result = registry.getDatabaseStrategies();

        assertThat(result).containsOnlyKeys("postgres", "oracle");
        assertThat(result.get("postgres")).isSameAs(postgres);
        assertThat(result.get("oracle")).isSameAs(oracle);
    }

    @Test
    void getDatabaseStrategies_lowercasesStrategyName() {
        DatabaseStrategy strategy = mock(DatabaseStrategy.class);
        when(strategy.getStrategyName()).thenReturn("POSTGRES");

        DefaultDatabaseStrategyRegistry registry = new DefaultDatabaseStrategyRegistry(List.of(strategy));

        Map<String, DatabaseStrategy> result = registry.getDatabaseStrategies();

        assertThat(result).containsOnlyKeys("postgres");
        assertThat(result.get("postgres")).isSameAs(strategy);
    }

    @Test
    void getDatabaseStrategies_lowercasesMixedCaseStrategyName() {
        DatabaseStrategy strategy = mock(DatabaseStrategy.class);
        when(strategy.getStrategyName()).thenReturn("MyCustomDb");

        DefaultDatabaseStrategyRegistry registry = new DefaultDatabaseStrategyRegistry(List.of(strategy));

        Map<String, DatabaseStrategy> result = registry.getDatabaseStrategies();

        assertThat(result).containsOnlyKeys("mycustomdb");
    }
}
