package com.roervik.tdt4145.dbproject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Program {
    private static Properties programProperties;

    public static String getProperty(final String propertyName) {
        if (programProperties == null) {
            loadProperties();
        }
        return programProperties.getProperty(propertyName);
    }

    private static void loadProperties() {
        InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream("program.properties");
        programProperties = new Properties();
        try {
            programProperties.load(input);
        } catch (IOException e) {
            System.out.println("Unable to load program properties");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
