package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.DBConnection;
import com.roervik.tdt4145.dbproject.model.ExerciseGroup;
import com.roervik.tdt4145.dbproject.util.NamedParameterStatement;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;

import static com.roervik.tdt4145.dbproject.Program.exerciseDBManager;
import static com.roervik.tdt4145.dbproject.Program.exerciseWithEquipmentDBManager;
import static com.roervik.tdt4145.dbproject.util.StreamUtils.uncheckCall;
import static com.roervik.tdt4145.dbproject.util.StreamUtils.uncheckRun;

public class ExerciseGroupDBManager extends DBConnection {
    public ExerciseGroupDBManager() throws Exception {
        super();
    }

    public Optional<ExerciseGroup> getExerciseGroupById(final UUID groupId) throws Exception {
        final String query = "select name from ExerciseGroup" +
                " where groupId = :groupId:";
        final NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("groupId", groupId.toString());
        final ResultSet result = statement.getStatement().executeQuery();
        if(!result.first()) {
            return Optional.empty();
        }
        final ExerciseGroup exerciseGroup = ExerciseGroup.builder()
                .groupId(groupId)
                .name(result.getString("name"))
                .exercises(exerciseDBManager.getExercisesInExerciseGroup(groupId))
                .exercisesWithEquipment(exerciseWithEquipmentDBManager
                        .getExerciseWithEquipmentsInExerciseGroup(groupId))
                .build();
        return Optional.of(exerciseGroup);
    }

    public void addExerciseToExerciseGroup(final UUID exerciseId, final UUID groupId) throws Exception {
        String query = "insert into ExerciseInGroup (groupId, exerciseId)" +
                " values(:groupId:, :exerciseId:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("groupId", groupId.toString());
        statement.setString("exerciseId", exerciseId.toString());
        statement.getStatement().executeUpdate();
    }

    public void addExerciseWithEquipmentToExerciseGroup(final UUID exerciseId, final UUID groupId) throws Exception {
        String query = "insert into ExerciseWithEquipmentInGroup (groupId, exerciseId)" +
                " values(:groupId:, :exerciseId:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("groupId", groupId.toString());
        statement.setString("exerciseId", exerciseId.toString());
        statement.getStatement().executeUpdate();
    }

    public void createExerciseGroup(final ExerciseGroup exerciseGroup) throws Exception {
        String query = "insert into ExerciseGroup (groupId, name)" +
                " values (:groupId:, :name:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("groupId", exerciseGroup.getGroupId().toString());
        statement.setString("name", exerciseGroup.getName());
        statement.getStatement().executeUpdate();
        exerciseGroup.getExercises().stream()
                .filter(exercise -> uncheckCall(() ->
                        !exerciseDBManager.getExerciseById(exercise.getExerciseId()).isPresent()))
                .forEach(exercise -> uncheckRun(() -> exerciseDBManager.createExercise(exercise)));
        exerciseGroup.getExercisesWithEquipment().stream()
                .filter(exercise -> uncheckCall(() ->
                        !exerciseWithEquipmentDBManager
                                .getExerciseWithEquipmentById(exercise.getExerciseId()).isPresent()))
                .forEach(exercise -> uncheckRun(() -> exerciseWithEquipmentDBManager
                        .createExerciseWithEquipment(exercise)));

        exerciseGroup.getExercises().forEach(exercise -> uncheckRun(() ->
                addExerciseToExerciseGroup(exercise.getExerciseId(), exerciseGroup.getGroupId())));
        exerciseGroup.getExercisesWithEquipment().forEach(exercise -> uncheckRun(() ->
                addExerciseWithEquipmentToExerciseGroup(exercise.getExerciseId(), exerciseGroup.getGroupId())));
    }
}
