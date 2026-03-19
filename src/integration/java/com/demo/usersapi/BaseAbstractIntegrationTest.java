package com.demo.usersapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.oracle.OracleContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("integration-test")
public abstract class BaseAbstractIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("users_db")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("integration/init-postgres.sql");

    @Container
    static OracleContainer oracle = new OracleContainer("gvenzl/oracle-free:23-slim-faststart")
            .withInitScript("integration/init-oracle.sql");

    @DynamicPropertySource
    static void overrideDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("app.data-sources[0].url", postgres::getJdbcUrl);
        registry.add("app.data-sources[0].user", postgres::getUsername);
        registry.add("app.data-sources[0].password", postgres::getPassword);

        registry.add("app.data-sources[1].url", oracle::getJdbcUrl);
        registry.add("app.data-sources[1].user", oracle::getUsername);
        registry.add("app.data-sources[1].password", oracle::getPassword);
    }

    @Autowired
    protected TestRestTemplate restTemplate;
}
