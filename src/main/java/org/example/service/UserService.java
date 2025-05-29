package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;

    // Используется в основном приложении
    public UserService() {
        this.userDao = new UserDao();
    }

    // Используется для тестов
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(User user) {
        Objects.requireNonNull(user, "Пользователь не должен быть null");
        logger.debug("Создание пользователя: {}", user);
        userDao.createUser(user);
    }

    public User getUserById(Long id) {
        logger.debug("Получение пользователя по ID: {}", id);
        return userDao.getUserById(id);
    }

    public List<User> getAllUsers() {
        logger.debug("Получение всех пользователей");
        return userDao.getAllUsers();
    }

    public void updateUser(User user) {
        Objects.requireNonNull(user, "Пользователь не должен быть null");
        logger.debug("Обновление пользователя: {}", user);
        userDao.updateUser(user);
    }

    public void deleteUser(Long id) {
        logger.debug("Удаление пользователя с ID: {}", id);
        userDao.deleteUser(id);
    }
}
