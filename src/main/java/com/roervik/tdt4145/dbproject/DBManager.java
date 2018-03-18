package com.roervik.tdt4145.dbproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;

public abstract class DBManager<T> {
    protected final Connection connection;

    public abstract Optional<T> getById(UUID objectId) throws Exception;

    public abstract void create(T object) throws Exception;

    public abstract List<T> getAll() throws Exception;

    private static Connection openConnectionFromProperties(String propertiesFile) throws Exception {
        InputStream input = DBManager.class.getResourceAsStream(propertiesFile);
        Properties connectionProps = new Properties();
        try {
            connectionProps.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Class.forName(connectionProps.getProperty("driver")).newInstance();
        Connection connection = DriverManager.getConnection(
                connectionProps.getProperty("dbname"),
                connectionProps);
        if (Objects.isNull(connection.getCatalog())
                || connection.getCatalog().isEmpty()) {
            connection.setCatalog(connectionProps.getProperty("catalog"));
        }
        if (Objects.isNull(connection.getSchema())
                || connection.getSchema().isEmpty()) {
            connection.setSchema(connectionProps.getProperty("schema"));
        }
        return connection;
    }

    public void loadCreateScript() throws Exception {
        try {
            InputStream input = DBManager.class
                    .getResourceAsStream("CreateDatabase.sql");

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }

            System.out.println("Running create script");
            Statement statement = connection.createStatement();
            statement.execute(out.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DBManager(String propertiesFile) throws Exception {
        connection = openConnectionFromProperties(propertiesFile);
    }

    public DBManager() throws Exception {
        this(Program.getProperty("dbconnection"));
    }

    public void closeConnection() throws Exception {
        connection.close();
    }
}
