package com.demo.usersapi.service;

import com.demo.usersapi.config.DataSourceProperties;
import com.demo.usersapi.mapper.UserMapper;
import com.demo.usersapi.repository.UserRepository;
import com.example.aggregator.model.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAggregationService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final DataSourceProperties dataSourcesProperties;

    public List<UserDto> fetchAllUsers() {
        log.info("Starting users aggregation from {} data sources", dataSourcesProperties.getDataSources().size());

        var futures = dataSourcesProperties.getDataSources().stream()
                .map(userRepository::findAllUsersAsync)
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .map(userMapper::toDto)
                .toList();
    }
}
