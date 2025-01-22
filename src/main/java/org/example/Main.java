package org.example;

import org.example.controller.UserController;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        port(8080);

        // Configuración de CORS
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "GET,POST,PUT,DELETE,OPTIONS");
            response.type("application/json");
        });

        // Inicializar controlador
        new UserController();

    }
}