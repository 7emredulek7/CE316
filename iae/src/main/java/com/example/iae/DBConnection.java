package com.example.iae;
import java.io.File;
import java.sql.*;
public class DBConnection {
    private static DBConnection instance = null;
    private final String fileName;
    private Connection conn;

    private DBConnection() {
        this.fileName = "info.db";
        File file = new File(fileName);
        boolean firstRun = !file.exists();
        conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
            if (firstRun) {
                Statement stat = conn.createStatement();

                stat.executeUpdate("CREATE TABLE PROJECT(" +
                        "NAME TEXT PRIMARY KEY," +
                        "INPUT_FILE TEXT NOT NULL," +
                        "OUTPUT_FILE TEXT NOT NULL," +
                        "CONFIG_FILE TEXT NOT NULL)");

                stat.executeUpdate("CREATE TABLE STUDENT(" +
                        "PROJECT_NAME TEXT NOT NULL," +
                        "ID TEXT NOT NULL," +
                        "IS_PASSED INT NOT NULL," +
                        "PRIMARY KEY(PROJECT_NAME, ID) )");

            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static DBConnection getInstance() {

        if (instance == null) {
            instance = new DBConnection();
        }

        return instance;
    }

}

