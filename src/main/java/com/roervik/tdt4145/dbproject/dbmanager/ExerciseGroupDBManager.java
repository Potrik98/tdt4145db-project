package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.DBManager;
import com.roervik.tdt4145.dbproject.model.ExerciseGroup;
import com.roervik.tdt4145.dbproject.util.NamedParameterStatement;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.roervik.tdt4145.dbproject.Program.exerciseDBManager;
import static com.roervik.tdt4145.dbproject.Program.exerciseWithEquipmentDBManager;
import static com.roervik.tdt4145.dbproject.util.StreamUtils.uncheckCall;
import static com.roervik.tdt4145.dbproject.util.StreamUtils.uncheckRun;

public class ExerciseGroupDBManager extends DBManager<ExerciseGroup> {
    public ExerciseGroupDBManager() throws Exception {
        super();
    }

    public Optional<ExerciseGroup> getById(final UUID groupId) throws Exception {
        final String query = "select groupName from ExerciseGroup" +
                " where groupId = :groupId:";
        final NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("groupId", groupId.toString());
        final ResultSet result = statement.getStatement().executeQuery();
        if(!result.first()) {
            return Optional.empty();
        }
        final ExerciseGroup exerciseGroup = ExerciseGroup.builder()
                .groupId(groupId)
                .name(result.getString("groupName"))
                .exercises(exerciseDBManager.getExercisesInExerciseGroup(groupId))
                .exercisesWithEquipment(exerciseWithEquipmentDBManager
                        .getExerciseWithEquipmentsInExerciseGroup(groupId))
                .build();
        return Optional.of(exerciseGroup);
    }

    public List<ExerciseGroup> getAll() throws Exception {
        final String query = "select groupId, groupName from ExerciseGroup;";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        final ResultSet result = statement.getStatement().executeQuery();
        final List<ExerciseGroup> exerciseGroups = new ArrayList<>();
        while(result.next()) {
            UUID groupId = UUID.fromString(result.getString("groupId"));
            final ExerciseGroup exerciseGroup = ExerciseGroup.builder()
                    .groupId(groupId)
                    .name(result.getString("groupName"))
                    .exercises(exerciseDBManager.getExercisesInExerciseGroup(groupId))
                    .exercisesWithEquipment(exerciseWithEquipmentDBManager
                            .getExerciseWithEquipmentsInExerciseGroup(groupId))
                    .build();
            exerciseGroups.add(exerciseGroup);
        }
        return ExerciseGroup.ordering.immutableSortedCopy(exerciseGroups);
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

    public void create(final ExerciseGroup exerciseGroup) throws Exception {
        String query = "insert into ExerciseGroup (groupId, groupName)" +
                " values (:groupId:, :groupName:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("groupId", exerciseGroup.getGroupId().toString());
        statement.setString("groupName", exerciseGroup.getName());
        statement.getStatement().executeUpdate();
        exerciseGroup.getExercises().stream()
                .filter(exercise -> uncheckCall(() ->
                        !exerciseDBManager.getById(exercise.getExerciseId()).isPresent()))
                .forEach(exercise -> uncheckRun(() -> exerciseDBManager.create(exercise)));
        exerciseGroup.getExercisesWithEquipment().stream()
                .filter(exercise -> uncheckCall(() ->
                        !exerciseWithEquipmentDBManager
                                .getById(exercise.getExerciseId()).isPresent()))
                .forEach(exercise -> uncheckRun(() -> exerciseWithEquipmentDBManager
                        .create(exercise)));

        exerciseGroup.getExercises().forEach(exercise -> uncheckRun(() ->
                addExerciseToExerciseGroup(exercise.getExerciseId(), exerciseGroup.getGroupId())));
        exerciseGroup.getExercisesWithEquipment().forEach(exercise -> uncheckRun(() ->
                addExerciseWithEquipmentToExerciseGroup(exercise.getExerciseId(), exerciseGroup.getGroupId())));
    }
}
