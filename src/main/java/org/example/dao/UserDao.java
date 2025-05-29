package org.example.dao;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private final SessionFactory sessionFactory;

    // Используется в приложении
    public UserDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void createUser(User user) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            logger.info("Пользователь создан: {}", user);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Не удалось создать пользователя", e);
            throw e;
        }
    }

    public User getUserById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(User.class, id);
        } catch (Exception e) {
            logger.error("Не удалось получить пользователя с ID {}", id, e);
            throw e;
        }
    }

    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            logger.error("Не удалось получить всех пользователей", e);
            throw e;
        }
    }

    public void updateUser(User user) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            logger.info("Пользователь обновлен: {}", user);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Не удалось обновить пользователя", e);
            throw e;
        }
    }

    public void deleteUser(Long id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, id);
            if (user != null) {
                tx = session.beginTransaction();
                session.remove(user);
                tx.commit();
                logger.info("Пользователь удален: {}", user);
            } else {
                logger.warn("Пользователь с ID {} не найден для удаления", id);
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Не удалось удалить пользователя с ID {}", id, e);
            throw e;
        }
    }
}
