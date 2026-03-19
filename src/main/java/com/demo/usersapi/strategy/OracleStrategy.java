package com.demo.usersapi.strategy;

import org.springframework.stereotype.Component;

@Component
public class OracleStrategy extends DefaultDatabaseStrategy {

    @Override
    public String getStrategyName() {
        return "oracle";
    }
}
