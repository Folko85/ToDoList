package main;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import lombok.Getter;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:tc:postgresql:9.6-alpine:///test",
                "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
                "spring.datasource.username=test",
                "spring.datasource.password=test",
        })
public abstract class AbstractMainTest {

    private static final String TEST = "test";

    @Container
    private static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:9.6-alpine")
            .withDatabaseName(TEST)
            .withUsername(TEST)
            .withPassword(TEST);

    /**
     * Web application context.
     */
    @Autowired
    protected WebApplicationContext ctx;

    /**
     * Mock mvc.
     */
    protected MockMvc mockMvc;

    /**
     * Object mapper.
     */
    @Autowired
    @Getter
    private ObjectMapper mapper;

    /**
     * Create mock mvc.
     */
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(ctx)
                .build();
    }

}
