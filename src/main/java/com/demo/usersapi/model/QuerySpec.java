package com.demo.usersapi.model;

import java.util.Map;

public record QuerySpec(String sql, Map<String, Object> params) {}
