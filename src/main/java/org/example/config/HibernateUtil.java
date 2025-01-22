package org.example.config;

import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
            loadInitialData();
        } catch (Exception e) {
            System.err.println("Error inicializando SessionFactory:");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    private static void loadInitialData() {
        try (Session session = sessionFactory.openSession()) {
            String[] statements = new String(Files.readAllBytes(
                    Paths.get(HibernateUtil.class.getClassLoader()
                            .getResource("data.sql").toURI()))).split(";");

            //System.out.println("SQL a ejecutar: " + statements);
            session.beginTransaction();
            for(String sql : statements) {
                if(!sql.trim().isEmpty()) {
                    session.createNativeQuery(sql.trim(), User.class).executeUpdate();
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
