package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.DBConnection;
import com.roervik.tdt4145.dbproject.model.Exercise;
import com.roervik.tdt4145.dbproject.model.Exercise;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ExerciseDBManager extends DBConnection {
    public ExerciseDBManager() throws Exception {
        super();
    }

    public Optional<Exercise> getExerciseById(final UUID exerciseId) throws Exception {
        final String query = "select name, description from Exercise" +
                " where exerciseId = :exerciseId:";
        final NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("exerciseId", exerciseId.toString());
        final ResultSet result = statement.getStatement().executeQuery();
        if(!result.first()) {
            return Optional.empty();
        }
        final Exercise exercise = Exercise.builder()
                .exerciseId(exerciseId)
                .name(result.getString("name"))
                .description(result.getString("description"))
                .build();
        return Optional.of(exercise);
    }

    public List<Exercise> getExercisesInWorkout(final UUID workoutId) throws Exception {
        final String query = "select exerciseId, name, description from Exercise" +
                " join ExerciseInWorkout" +
                " on Exercise.exerciseId = ExerciseInWorkout.exerciseId" +
                " where workoutId = :workoutId:";
        final NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("workoutId", workoutId.toString());
        final ResultSet result = statement.getStatement().executeQuery();
        final List<Exercise> exercises = new ArrayList<>();
        while(result.next()) {
            final Exercise exercise = Exercise.builder()
                    .exerciseId(UUID.fromString(result.getString("exerciseId")))
                    .name(result.getString("name"))
                    .description(result.getString("description"))
                    .build();
            exercises.add(exercise);
        }
        return exercises;
    }

    public void createExercise(final Exercise exercise) throws Exception {
        String query = "insert into Exercise (exerciseId, name, description)" +
                " values (:exerciseId:, :name:, :description:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("exerciseId", exercise.getExerciseId().toString());
        statement.setString("name", exercise.getName());
        statement.setString("description", exercise.getDescription());
        statement.getStatement().executeUpdate();
    }
}
