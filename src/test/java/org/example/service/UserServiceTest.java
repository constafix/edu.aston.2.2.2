package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        userService = new UserService(userDao);
    }

    @Test
    void testCreateUser() {
        User user = new User("Alice", "alice@example.com", 30);
        userService.createUser(user);
        verify(userDao, times(1)).createUser(user);
    }

    @Test
    void testCreateUser_Null() {
        assertThrows(NullPointerException.class, () -> userService.createUser(null));
    }

    @Test
    void testGetUserById() {
        User user = new User("Bob", "bob@example.com", 25);
        user.setId(1L);
        when(userDao.getUserById(1L)).thenReturn(user);

        User result = userService.getUserById(1L);
        assertNotNull(result);
        assertEquals("Bob", result.getName());
        verify(userDao).getUserById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userDao.getUserById(999L)).thenReturn(null);
        User result = userService.getUserById(999L);
        assertNull(result);
    }

    @Test
    void testGetAllUsers() {
        List<User> mockUsers = List.of(new User("Alice", "alice@example.com", 30));
        when(userDao.getAllUsers()).thenReturn(mockUsers);

        List<User> result = userService.getAllUsers();
        assertEquals(1, result.size());
        verify(userDao).getAllUsers();
    }

    @Test
    void testGetAllUsers_Empty() {
        when(userDao.getAllUsers()).thenReturn(Collections.emptyList());

        List<User> result = userService.getAllUsers();
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateUser() {
        User user = new User("Charlie", "charlie@example.com", 40);
        user.setId(2L);
        userService.updateUser(user);
        verify(userDao, times(1)).updateUser(user);
    }

    @Test
    void testUpdateUser_Null() {
        assertThrows(NullPointerException.class, () -> userService.updateUser(null));
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(3L);
        verify(userDao, times(1)).deleteUser(3L);
    }

    @Test
    void testDeleteUser_InvalidId() {
        doNothing().when(userDao).deleteUser(999L);
        userService.deleteUser(999L);
        verify(userDao).deleteUser(999L);
    }
}
