package org.example.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDao {

    private static final Logger logger = LogManager.getLogger(UserDao.class);

    public void createUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();
            logger.info("User created: {}", user);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Failed to create user: {}", e.getMessage(), e);
        }
    }

    public User getUser(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                logger.info("User retrieved: {}", user);
            } else {
                logger.info("User with id {} not found", id);
            }
            return user;
        } catch (Exception e) {
            logger.error("Failed to get user: {}", e.getMessage(), e);
            return null;
        }
    }

    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();
            logger.info("Retrieved {} users", users.size());
            return users;
        } catch (Exception e) {
            logger.error("Failed to get all users: {}", e.getMessage(), e);
            return null;
        }
    }

    public void updateUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(user);

            transaction.commit();
            logger.info("User updated: {}", user);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Failed to update user: {}", e.getMessage(), e);
        }
    }

    public void deleteUser(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                logger.info("User deleted: {}", user);
            } else {
                logger.info("User with id {} not found for deletion", id);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Failed to delete user: {}", e.getMessage(), e);
        }
    }
}
