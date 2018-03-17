package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.DBConnection;
import com.roervik.tdt4145.dbproject.model.ExerciseWithEquipment;
import com.roervik.tdt4145.dbproject.util.NamedParameterStatement;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.roervik.tdt4145.dbproject.Program.equipmentDBManager;

public class ExerciseWithEquipmentDBManager extends DBConnection {
    public ExerciseWithEquipmentDBManager() throws Exception {
        super();
    }

    public Optional<ExerciseWithEquipment> getExerciseWithEquipmentById(final UUID exerciseId) throws Exception {
        final String query = "select equipmentId, description from ExerciseWithEquipment" +
                " where exerciseId = :exerciseId:";
        final NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("exerciseId", exerciseId.toString());
        final ResultSet result = statement.getStatement().executeQuery();
        if(!result.first()) {
            return Optional.empty();
        }
        final ExerciseWithEquipment exerciseWithEquipment = ExerciseWithEquipment.builder()
                .exerciseId(exerciseId)
                .description(result.getString("description"))
                .equipment(equipmentDBManager.getEquipmentById(
                        UUID.fromString(result.getString("equipmentId"))
                ).get())
                .build();
        return Optional.of(exerciseWithEquipment);
    }

    public List<ExerciseWithEquipment> getExerciseWithEquipmentsInWorkout(final UUID workoutId) throws Exception {
        final String query = "select ExerciseWithEquipment.exerciseId, equipmentId, description from ExerciseWithEquipment" +
                " join ExerciseWithEquipmentInWorkout" +
                " on ExerciseWithEquipment.exerciseId = ExerciseWithEquipmentInWorkout.exerciseId" +
                " where workoutId = :workoutId:" +
                " order by ExerciseWithEquipment.exerciseId;";
        final NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("workoutId", workoutId.toString());
        final ResultSet result = statement.getStatement().executeQuery();
        final List<ExerciseWithEquipment> exerciseWithEquipments = new ArrayList<>();
        while(result.next()) {
            final ExerciseWithEquipment exerciseWithEquipment = ExerciseWithEquipment.builder()
                    .exerciseId(UUID.fromString(result.getString("exerciseId")))
                    .description(result.getString("description"))
                    .equipment(equipmentDBManager.getEquipmentById(
                            UUID.fromString(result.getString("equipmentId"))
                    ).get())
                    .build();
            exerciseWithEquipments.add(exerciseWithEquipment);
        }
        return exerciseWithEquipments;
    }

    public void createExerciseWithEquipment(final ExerciseWithEquipment exerciseWithEquipment) throws Exception {
        if (!equipmentDBManager.getEquipmentById(exerciseWithEquipment.getEquipment().getEquipmentId()).isPresent()) {
            equipmentDBManager.createEquipment(exerciseWithEquipment.getEquipment());
        }
        String query = "insert into ExerciseWithEquipment (exerciseId, equipmentId, description)" +
                " values (:exerciseId:, :equipmentId:, :description:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("exerciseId", exerciseWithEquipment.getExerciseId().toString());
        statement.setString("equipmentId", exerciseWithEquipment.getEquipment().getEquipmentId().toString());
        statement.setString("description", exerciseWithEquipment.getDescription());
        statement.getStatement().executeUpdate();
    }
}
