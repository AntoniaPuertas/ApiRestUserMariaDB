package org.example.controller;

import com.google.gson.Gson;
import org.example.config.HibernateUtil;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import static spark.Spark.*;
import static spark.Spark.delete;

public class UserController {
    private final Gson gson = new Gson();

    public UserController() {
        // Definir rutas usando expresiones lambda
        get("/users", (request, response) -> {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return gson.toJson(session.createQuery("from User", User.class).list());
            }
        });

        get("/users/:id", (request, response) -> {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Long id = Long.parseLong(request.params(":id"));
                User user = session.get(User.class, id);
                if (user == null) {
                    response.status(404);
                    return gson.toJson("Usuario no encontrado");
                }
                return gson.toJson(user);
            }
        });

        post("/users", (request, response) -> {
            User user = gson.fromJson(request.body(), User.class);
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.persist(user);
                transaction.commit();
                response.status(201);
                return gson.toJson(user);
            }
        });

        put("/users/:id", (request, response) -> {
            User updates = gson.fromJson(request.body(), User.class);
            Long id = Long.parseLong(request.params(":id"));

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                User user = session.get(User.class, id);
                if (user == null) {
                    response.status(404);
                    return gson.toJson("Usuario no encontrado");
                }

                user.setName(updates.getName());
                user.setEmail(updates.getEmail());
                session.merge(user);
                transaction.commit();
                return gson.toJson(user);
            }
        });

        delete("/users/:id", (request, response) -> {
            Long id = Long.parseLong(request.params(":id"));
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                User user = session.get(User.class, id);
                if (user == null) {
                    response.status(404);
                    return gson.toJson("Usuario no encontrado");
                }
                session.remove(user);
                transaction.commit();
                response.status(204);
                return "";
            }
        });
    }
}
