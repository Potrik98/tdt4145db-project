package com.roervik.tdt4145.dbproject;

import com.roervik.tdt4145.dbproject.dbmanager.EquipmentDBManager;
import com.roervik.tdt4145.dbproject.dbmanager.ExerciseDBManager;
import com.roervik.tdt4145.dbproject.dbmanager.ExerciseWithEquipmentDBManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Program {
    private static Properties programProperties;

    public static ExerciseDBManager exerciseDBManager;
    public static ExerciseWithEquipmentDBManager exerciseWithEquipmentDBManager;
    public static EquipmentDBManager equipmentDBManager;

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

    public static void init() throws Exception {
        exerciseDBManager = new ExerciseDBManager();
        exerciseWithEquipmentDBManager = new ExerciseWithEquipmentDBManager();
        equipmentDBManager = new EquipmentDBManager();
    }

    public static void closeConnections() throws Exception {
        exerciseDBManager.closeConnection();
        exerciseWithEquipmentDBManager.closeConnection();
        equipmentDBManager.closeConnection();
    }

    public static void main(String[] args) throws Exception {
        init();
    }
}
