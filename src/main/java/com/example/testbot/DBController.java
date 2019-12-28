package com.example.testbot;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBController {
    private final String DATABASE_URL;
    private final String JDBC_DRIVER;

    private final String USER;
    private final String PASSWORD;

    private Connection connection;

    public DBController() {
        System.out.println("Загрузка настроек БД...");
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/resources/app.properties"));
            System.out.println("Настройки БД загружены");
        } catch (IOException e) {
            System.out.println("Не удалось загрузить настройки подключения к БД.");
            e.printStackTrace();
        }

        DATABASE_URL = prop.getProperty("db.url");
        JDBC_DRIVER = prop.getProperty("db.driverClassName");
        USER = prop.getProperty("db.username");
        PASSWORD = prop.getProperty("db.password");

        try {
            System.out.println("Подключение к БД...");
            getConnection();
            initiate();
            System.out.println("Поключение к БД установлено");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Не удалось подключиться к БД");
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        if (connection != null) {
            connection.close();
            connection = null;
        }

        System.out.println("Регистрация JDBC драйвера...");
        Class.forName(JDBC_DRIVER);

        System.out.println("Создание соединения к БД...");
        connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        System.out.println("Соединение к БД успешно");

        return connection;
    }

    private void initiate() throws SQLException {
        System.out.println("Создание необходимых таблиц в БД...");
        try (Statement statement = connection.createStatement()) {

            String SQL = "CREATE TABLE IF NOT EXISTS reminders " +
                    "(id SERIAL PRIMARY KEY, " +
                    " creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    " appointment_date TIMESTAMP NOT NULL, " +
                    " interval_date TIMESTAMP, " +
                    " regular BOOLEAN NOT NULL DEFAULT FALSE, " +
                    " user_id INTEGER NOT NULL, " +
                    " message VARCHAR(2000) NOT NULL)";

            statement.executeUpdate(SQL);
            System.out.println("Таблицы успешно созданы");
        }
    }
}
