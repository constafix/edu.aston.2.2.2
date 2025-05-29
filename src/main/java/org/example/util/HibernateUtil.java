package org.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {

            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();


            SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

            logger.info("SessionFactory успешно создана");
            return sessionFactory;
        } catch (Throwable ex) {
            logger.error("Ошибка создания SessionFactory: ", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        logger.info("Закрытие SessionFactory");
        getSessionFactory().close();
    }
}
