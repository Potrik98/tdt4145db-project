package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.DBManager;
import com.roervik.tdt4145.dbproject.model.ExerciseWithEquipment;
import com.roervik.tdt4145.dbproject.util.NamedParameterStatement;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.roervik.tdt4145.dbproject.Program.equipmentDBManager;

public class ExerciseWithEquipmentDBManager extends DBManager<ExerciseWithEquipment> {
    public ExerciseWithEquipmentDBManager() throws Exception {
        super();
    }

    public List<ExerciseWithEquipment> getAll() throws Exception {
        final String query = "select exerciseId, equipmentId, description from ExerciseWithEquipment;";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        final ResultSet result = statement.getStatement().executeQuery();
        final List<ExerciseWithEquipment> exerciseWithEquipments = new ArrayList<>();
        while(result.next()) {
            final UUID exerciseId = UUID.fromString(result.getString("exerciseId"));
            final ExerciseWithEquipment exerciseWithEquipment = ExerciseWithEquipment.builder()
                    .exerciseId(exerciseId)
                    .description(result.getString("description"))
                    .equipment(equipmentDBManager.getById(
                            UUID.fromString(result.getString("equipmentId"))
                    ).get())
                    .build();
            exerciseWithEquipments.add(exerciseWithEquipment);
        }
        return ExerciseWithEquipment.ordering.immutableSortedCopy(exerciseWithEquipments);
    }

    public Optional<ExerciseWithEquipment> getById(final UUID exerciseId) throws Exception {
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
                .equipment(equipmentDBManager.getById(
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
                    .equipment(equipmentDBManager.getById(
                            UUID.fromString(result.getString("equipmentId"))
                    ).get())
                    .build();
            exerciseWithEquipments.add(exerciseWithEquipment);
        }
        return ExerciseWithEquipment.ordering.immutableSortedCopy(exerciseWithEquipments);
    }

    public List<ExerciseWithEquipment> getExerciseWithEquipmentsInExerciseGroup(final UUID groupId) throws Exception {
        final String query = "select ExerciseWithEquipment.exerciseId, equipmentId, description from ExerciseWithEquipment" +
                " join ExerciseWithEquipmentInGroup" +
                " on ExerciseWithEquipment.exerciseId = ExerciseWithEquipmentInGroup.exerciseId" +
                " where groupId = :groupId:" +
                " order by ExerciseWithEquipment.exerciseId;";
        final NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("groupId", groupId.toString());
        final ResultSet result = statement.getStatement().executeQuery();
        final List<ExerciseWithEquipment> exerciseWithEquipments = new ArrayList<>();
        while(result.next()) {
            final ExerciseWithEquipment exerciseWithEquipment = ExerciseWithEquipment.builder()
                    .exerciseId(UUID.fromString(result.getString("exerciseId")))
                    .description(result.getString("description"))
                    .equipment(equipmentDBManager.getById(
                            UUID.fromString(result.getString("equipmentId"))
                    ).get())
                    .build();
            exerciseWithEquipments.add(exerciseWithEquipment);
        }
        return ExerciseWithEquipment.ordering.immutableSortedCopy(exerciseWithEquipments);
    }

    public void create(final ExerciseWithEquipment exerciseWithEquipment) throws Exception {
        if (!equipmentDBManager.getById(exerciseWithEquipment.getEquipment().getEquipmentId()).isPresent()) {
            equipmentDBManager.create(exerciseWithEquipment.getEquipment());
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
