package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.DBConnection;
import com.roervik.tdt4145.dbproject.Program;
import com.roervik.tdt4145.dbproject.model.Workout;
import com.roervik.tdt4145.dbproject.util.NamedParameterStatement;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;

import static com.roervik.tdt4145.dbproject.Program.exerciseDBManager;
import static com.roervik.tdt4145.dbproject.Program.exerciseWithEquipmentDBManager;
import static com.roervik.tdt4145.dbproject.util.StreamUtils.uncheckCall;
import static com.roervik.tdt4145.dbproject.util.StreamUtils.uncheckRun;

public class WorkoutDBManager extends DBConnection {
    public WorkoutDBManager() throws Exception {
        super();
    }

    public Optional<Workout> getWorkoutById(final UUID workoutId) throws Exception {
        final String query = "select performance, personalShape, startTime, endTime from Workout" +
                " where workoutId = :workoutId:;";
        final NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("workoutId", workoutId.toString());
        final ResultSet result = statement.getStatement().executeQuery();
        if(!result.first()) {
            return Optional.empty();
        }
        final Workout workout = Workout.builder()
                .workoutId(workoutId)
                .performance(result.getInt("performance"))
                .personalShape(result.getInt("personalShape"))
                .startTime(result.getTimestamp("startTime").toLocalDateTime())
                .endTime(result.getTimestamp("endTime").toLocalDateTime())
                .exercises(exerciseDBManager.getExercisesInWorkout(workoutId))
                .exercisesWithEquipment(Program.exerciseWithEquipmentDBManager
                        .getExerciseWithEquipmentsInWorkout(workoutId))
                .build();
        return Optional.of(workout);
    }

    public void addExerciseToWorkout(final UUID exerciseId, final UUID workoutId) throws Exception {
        String query = "insert into ExerciseInWorkout (workoutId, exerciseId)" +
                " values(:workoutId:, :exerciseId:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("workoutId", workoutId.toString());
        statement.setString("exerciseId", exerciseId.toString());
        statement.getStatement().executeUpdate();
    }

    public void addExerciseWithEquipmentToWorkout(final UUID exerciseId, final UUID workoutId) throws Exception {
        String query = "insert into ExerciseWithEquipmentInWorkout (workoutId, exerciseId)" +
                " values(:workoutId:, :exerciseId:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("workoutId", workoutId.toString());
        statement.setString("exerciseId", exerciseId.toString());
        statement.getStatement().executeUpdate();
    }

    public void createWorkout(final Workout workout) throws Exception {
        String query = "insert into Workout (workoutId, performance, personalShape, startTime, endTime)" +
                " values (:workoutId:, :performance:, :personalShape:, :startTime:, :endTime:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("workoutId", workout.getWorkoutId().toString());
        statement.setInt("performance", workout.getPerformance());
        statement.setInt("personalShape", workout.getPersonalShape());
        statement.setTimestamp("startTime", workout.getStartTime());
        statement.setTimestamp("endTime", workout.getEndTime());
        statement.getStatement().executeUpdate();

        workout.getExercises().stream()
                .filter(exercise -> uncheckCall(() ->
                        !exerciseDBManager.getExerciseById(exercise.getExerciseId()).isPresent()))
                .forEach(exercise -> uncheckRun(() -> exerciseDBManager.createExercise(exercise)));
        workout.getExercisesWithEquipment().stream()
                .filter(exercise -> uncheckCall(() ->
                        !exerciseWithEquipmentDBManager
                                .getExerciseWithEquipmentById(exercise.getExerciseId()).isPresent()))
                .forEach(exercise -> uncheckRun(() -> exerciseWithEquipmentDBManager
                        .createExerciseWithEquipment(exercise)));

        workout.getExercises().forEach(exercise -> uncheckRun(() ->
                addExerciseToWorkout(exercise.getExerciseId(), workout.getWorkoutId())));
        workout.getExercisesWithEquipment().forEach(exercise -> uncheckRun(() ->
                addExerciseWithEquipmentToWorkout(exercise.getExerciseId(), workout.getWorkoutId())));
    }
}