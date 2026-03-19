package com.demo.usersapi.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class MappingConfig {

    @NotBlank private String id;
    @NotBlank private String username;
    @NotBlank private String name;
    @NotBlank private String surname;

    public Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("username", username);
        map.put("name", name);
        map.put("surname", surname);
        return map;
    }
}
