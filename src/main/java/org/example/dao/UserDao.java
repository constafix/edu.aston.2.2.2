package org.example.dao;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.log4j.Logger;

import java.util.List;

public class UserDao {
    private static final Logger logger = Logger.getLogger(UserDao.class);

    public void createUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            logger.info("Пользователь успешно создан: " + user);
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при создании пользователя", e);
        }
    }

    public User getUser(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                logger.info("Найден пользователь: " + user);
            } else {
                logger.info("Пользователь с ID " + id + " не найден");
            }
            return user;
        } catch (HibernateException e) {
            logger.error("Ошибка при получении пользователя", e);
            return null;
        }
    }

    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("FROM User", User.class).list();
            logger.info("Получен список пользователей: " + users.size());
            return users;
        } catch (HibernateException e) {
            logger.error("Ошибка при получении списка пользователей", e);
            return null;
        }
    }

    public void updateUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
            logger.info("Пользователь успешно обновлен: " + user);
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при обновлении пользователя", e);
        }
    }

    public void deleteUser(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                transaction = session.beginTransaction();
                session.delete(user);
                transaction.commit();
                logger.info("Пользователь успешно удалён: " + user);
            } else {
                logger.warn("Пользователь с ID " + id + " не найден для удаления");
            }
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при удалении пользователя", e);
        }
    }
}
