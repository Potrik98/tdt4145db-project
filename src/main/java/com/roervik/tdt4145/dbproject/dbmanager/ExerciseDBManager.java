package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.DBManager;
import com.roervik.tdt4145.dbproject.model.Exercise;
import com.roervik.tdt4145.dbproject.util.NamedParameterStatement;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ExerciseDBManager extends DBManager<Exercise> {
    public ExerciseDBManager() throws Exception {
        super();
    }

    public List<Exercise> getAll() throws Exception {
        final String query = "select exerciseId, exerciseName, description from Exercise;";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        final ResultSet result = statement.getStatement().executeQuery();
        final List<Exercise> exercises = new ArrayList<>();
        while(result.next()) {
            final Exercise exercise = Exercise.builder()
                    .exerciseId(UUID.fromString(result.getString("exerciseId")))
                    .name(result.getString("exerciseName"))
                    .description(result.getString("description"))
                    .build();
            exercises.add(exercise);
        }
        return Exercise.ordering.immutableSortedCopy(exercises);
    }

    public Optional<Exercise> getById(final UUID exerciseId) throws Exception {
        final String query = "select exerciseName, description from Exercise" +
                " where exerciseId = :exerciseId:";
        final NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("exerciseId", exerciseId.toString());
        final ResultSet result = statement.getStatement().executeQuery();
        if(!result.first()) {
            return Optional.empty();
        }
        final Exercise exercise = Exercise.builder()
                .exerciseId(exerciseId)
                .name(result.getString("exerciseName"))
                .description(result.getString("description"))
                .build();
        return Optional.of(exercise);
    }

    public List<Exercise> getExercisesInWorkout(final UUID workoutId) throws Exception {
        final String query = "select Exercise.exerciseId, exerciseName, description from Exercise" +
                " join ExerciseInWorkout" +
                " on Exercise.exerciseId = ExerciseInWorkout.exerciseId" +
                " where workoutId = :workoutId:" +
                " order by Exercise.exerciseId;";
        final NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("workoutId", workoutId.toString());
        final ResultSet result = statement.getStatement().executeQuery();
        final List<Exercise> exercises = new ArrayList<>();
        while(result.next()) {
            final Exercise exercise = Exercise.builder()
                    .exerciseId(UUID.fromString(result.getString("exerciseId")))
                    .name(result.getString("exerciseName"))
                    .description(result.getString("description"))
                    .build();
            exercises.add(exercise);
        }
        return Exercise.ordering.immutableSortedCopy(exercises);
    }

    public List<Exercise> getExercisesInExerciseGroup(final UUID groupId) throws Exception {
        final String query = "select Exercise.exerciseId, exerciseName, description from Exercise" +
                " join ExerciseInGroup" +
                " on Exercise.exerciseId = ExerciseInGroup.exerciseId" +
                " where groupId = :groupId:" +
                " order by Exercise.exerciseId;";
        final NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("groupId", groupId.toString());
        final ResultSet result = statement.getStatement().executeQuery();
        final List<Exercise> exercises = new ArrayList<>();
        while(result.next()) {
            final Exercise exercise = Exercise.builder()
                    .exerciseId(UUID.fromString(result.getString("exerciseId")))
                    .name(result.getString("exerciseName"))
                    .description(result.getString("description"))
                    .build();
            exercises.add(exercise);
        }
        return Exercise.ordering.immutableSortedCopy(exercises);
    }

    public void create(final Exercise exercise) throws Exception {
        String query = "insert into Exercise (exerciseId, exerciseName, description)" +
                " values (:exerciseId:, :exerciseName:, :description:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("exerciseId", exercise.getExerciseId().toString());
        statement.setString("exerciseName", exercise.getName());
        statement.setString("description", exercise.getDescription());
        statement.getStatement().executeUpdate();
    }
}
