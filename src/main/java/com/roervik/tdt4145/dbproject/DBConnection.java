package com.roervik.tdt4145.dbproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class DBConnection {
    protected final Connection connection;

    private static Connection openConnectionFromProperties(String propertiesFile) throws Exception {
        InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream(propertiesFile);
        Properties connectionProps = new Properties();
        try {
            connectionProps.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Class.forName(connectionProps.getProperty("driver")).newInstance();
        System.out.println("Opening db connection to " + connectionProps.getProperty("dbname"));
        return DriverManager.getConnection(
                connectionProps.getProperty("dbname"),
                connectionProps);
    }

    public void loadCreateScript() throws Exception {
        InputStream input = ClassLoader.getSystemClassLoader()
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
    }

    public DBConnection(String propertiesFile) throws Exception {
        connection = openConnectionFromProperties(propertiesFile);
    }

    public void closeConnection() throws Exception {
        connection.close();
    }
}
