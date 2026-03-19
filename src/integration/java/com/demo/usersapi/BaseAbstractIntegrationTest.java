package com.demo.usersapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Testcontainers
public abstract class BaseAbstractIntegrationTest {

    private static final String COMPOSE_TEST_YAML = "src/integration/resources/integration/docker-compose-test.yml";

    @Container
    static final ComposeContainer container = new ComposeContainer(new File(COMPOSE_TEST_YAML))
            .withLocalCompose(true)
            .withExposedService("postgres-db", 5432,
                    Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60)))
            .withExposedService("mysql-db", 3306,
                    Wait.forHealthcheck().withStartupTimeout(Duration.ofSeconds(90)))
            .withExposedService("oracle-db", 1521,
                    Wait.forLogMessage(".*DATABASE IS READY TO USE!.*", 1)
                            .withStartupTimeout(Duration.ofMinutes(5)));

    @Autowired
    public TestRestTemplate restTemplate;
}
