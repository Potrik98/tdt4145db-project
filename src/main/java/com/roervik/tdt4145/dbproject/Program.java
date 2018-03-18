package com.roervik.tdt4145.dbproject;

import com.roervik.tdt4145.dbproject.dbmanager.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class Program {
    private static Properties programProperties;

    public static ExerciseDBManager exerciseDBManager;
    public static ExerciseWithEquipmentDBManager exerciseWithEquipmentDBManager;
    public static EquipmentDBManager equipmentDBManager;
    public static WorkoutDBManager workoutDBManager;
    public static ExerciseGroupDBManager exerciseGroupDBManager;

    public static String getProperty(final String propertyName) {
        if (programProperties == null) {
            loadProperties();
        }
        return programProperties.getProperty(propertyName);
    }

    private static void loadProperties() {
        InputStream input = Program.class.getResourceAsStream("program.properties");
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
        workoutDBManager = new WorkoutDBManager();
        exerciseGroupDBManager = new ExerciseGroupDBManager();
    }

    public static void closeConnections() throws Exception {
        exerciseDBManager.closeConnection();
        exerciseWithEquipmentDBManager.closeConnection();
        equipmentDBManager.closeConnection();
        workoutDBManager.closeConnection();
        exerciseGroupDBManager.closeConnection();
    }

    public static void main(String[] args) throws Exception {
        init();
        try {
            exerciseDBManager.loadCreateScript();
        } catch (Exception e) {
            e.printStackTrace();
            closeConnections();
            return;
        }
        boolean running = true;
        Scanner input = new Scanner(System.in);
        while (running) {
            String line = input.nextLine();
            running = !line.contains("q");
        }
        closeConnections();
    }
}
