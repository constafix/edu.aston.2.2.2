package org.example.service;

import org.example.dto.UserDto;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private static Long createdUserId;

    @Test
    @Order(1)
    void testCreateUser() {
        UserDto newUser = new UserDto(null, "Integration Test User", "inttest@example.com", 30);
        UserDto savedUser = userService.createUser(newUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Integration Test User");

        createdUserId = savedUser.getId();
    }

    @Test
    @Order(2)
    void testGetUserById() {
        UserDto user = userService.getUserById(createdUserId);
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("inttest@example.com");
    }
    @Test
    @Order(3)
    void testGetAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        assertThat(users).isNotEmpty();
    }

    @Test
    @Order(4)
    void testUpdateUser() {
        UserDto updateDto = new UserDto(null, "Updated Name", "updated@example.com", 35);
        UserDto updatedUser = userService.updateUser(createdUserId, updateDto);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getAge()).isEqualTo(35);
    }

    @Test
    @Order(5)
    void testDeleteUser() {
        boolean deleted = userService.deleteUser(createdUserId);
        assertThat(deleted).isTrue();

        UserDto user = userService.getUserById(createdUserId);
        assertThat(user).isNull();
    }
}
