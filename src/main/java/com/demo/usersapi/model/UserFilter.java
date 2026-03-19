package com.demo.usersapi.model;

public record UserFilter(String name, String username, String surname) {

    public static UserFilter empty() {
        return new UserFilter(null, null, null);
    }
}
