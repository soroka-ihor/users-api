package com.demo.usersapi.mapper;

import com.demo.usersapi.model.User;
import com.example.aggregator.model.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
