package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.model.Exercise;
import com.roervik.tdt4145.dbproject.model.ExerciseGroup;
import com.roervik.tdt4145.dbproject.model.ExerciseWithEquipment;
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

public class ExerciseGroupDBManager extends DBManagerWithRelation<ExerciseGroup> {
    public ExerciseGroupDBManager() throws Exception {
        super();
    }

    public List<Exercise> getRelatedExercises(UUID groupId) throws Exception {
        return exerciseDBManager.getExercisesInExerciseGroup(groupId);
    }

    public List<ExerciseWithEquipment> getRelatedExerciseWithEquipments(UUID groupId) throws Exception {
        return exerciseWithEquipmentDBManager.getExerciseWithEquipmentsInExerciseGroup(groupId);
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
                .exerciseWithEquipments(exerciseWithEquipmentDBManager
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
                    .exerciseWithEquipments(exerciseWithEquipmentDBManager
                            .getExerciseWithEquipmentsInExerciseGroup(groupId))
                    .build();
            exerciseGroups.add(exerciseGroup);
        }
        return ExerciseGroup.ordering.immutableSortedCopy(exerciseGroups);
    }

    public void addExercise(final UUID exerciseId, final UUID groupId) throws Exception {
        String query = "insert into ExerciseInGroup (groupId, exerciseId)" +
                " values(:groupId:, :exerciseId:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("groupId", groupId.toString());
        statement.setString("exerciseId", exerciseId.toString());
        statement.getStatement().executeUpdate();
    }

    public void addExerciseWithEquipment(final UUID exerciseId, final UUID groupId) throws Exception {
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
        exerciseGroup.getExerciseWithEquipments().stream()
                .filter(exercise -> uncheckCall(() ->
                        !exerciseWithEquipmentDBManager
                                .getById(exercise.getExerciseId()).isPresent()))
                .forEach(exercise -> uncheckRun(() -> exerciseWithEquipmentDBManager
                        .create(exercise)));

        exerciseGroup.getExercises().forEach(exercise -> uncheckRun(() ->
                addExercise(exercise.getExerciseId(), exerciseGroup.getGroupId())));
        exerciseGroup.getExerciseWithEquipments().forEach(exercise -> uncheckRun(() ->
                addExerciseWithEquipment(exercise.getExerciseId(), exerciseGroup.getGroupId())));
    }
}
