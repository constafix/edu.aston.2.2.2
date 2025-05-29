package org.example.dao;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoIntegrationTest {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private UserDao userDao;

    @BeforeAll
    void startContainer() {
        postgres.start();

        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());

        SessionFactory factory = HibernateUtil.getSessionFactory();
        userDao = new UserDao(factory);
    }

    @AfterAll
    void stopContainer() {
        postgres.stop();
    }

    @BeforeEach
    void cleanup() {
        // Чистка таблицы между тестами
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            var tx = session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            tx.commit();
        }
    }

    @Test
    void testCreateAndGetUser() {
        User user = new User("Test User", "test@example.com", 20);
        userDao.createUser(user);

        List<User> users = userDao.getAllUsers();
        assertEquals(1, users.size());
    }

    @Test
    void testUpdateUser() {
        User user = new User("Old", "old@example.com", 25);
        userDao.createUser(user);

        user.setName("New");
        userDao.updateUser(user);

        User updated = userDao.getAllUsers().get(0);
        assertEquals("New", updated.getName());
    }

    @Test
    void testDeleteUser() {
        User user = new User("To Delete", "delete@example.com", 35);
        userDao.createUser(user);

        Long id = userDao.getAllUsers().get(0).getId();
        userDao.deleteUser(id);

        assertTrue(userDao.getAllUsers().isEmpty());
    }
}
