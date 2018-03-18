package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.Program;
import com.roervik.tdt4145.dbproject.model.Workout;
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

public class WorkoutDBManager extends DBManagerWithRelation<Workout> {
    public WorkoutDBManager() throws Exception {
        super();
    }

    public Optional<Workout> getById(final UUID workoutId) throws Exception {
        final String query = "select performance, personalShape, note, startTime, endTime from Workout" +
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
                .note(result.getString("note"))
                .startTime(result.getTimestamp("startTime").toLocalDateTime())
                .endTime(result.getTimestamp("endTime").toLocalDateTime())
                .exercises(exerciseDBManager.getExercisesInWorkout(workoutId))
                .exerciseWithEquipments(Program.exerciseWithEquipmentDBManager
                        .getExerciseWithEquipmentsInWorkout(workoutId))
                .build();
        return Optional.of(workout);
    }

    public List<Workout> getAll() throws Exception {
        final String query = "select workoutId, performance, personalShape, note, startTime, endTime from Workout;";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        final ResultSet result = statement.getStatement().executeQuery();
        final List<Workout> workouts = new ArrayList<>();
        while(result.next()) {
            UUID workoutId = UUID.fromString(result.getString("workoutId"));
            final Workout workout = Workout.builder()
                    .workoutId(workoutId)
                    .performance(result.getInt("performance"))
                    .personalShape(result.getInt("personalShape"))
                    .note(result.getString("note"))
                    .startTime(result.getTimestamp("startTime").toLocalDateTime())
                    .endTime(result.getTimestamp("endTime").toLocalDateTime())
                    .exercises(exerciseDBManager.getExercisesInWorkout(workoutId))
                    .exerciseWithEquipments(Program.exerciseWithEquipmentDBManager
                            .getExerciseWithEquipmentsInWorkout(workoutId))
                    .build();
            workouts.add(workout);
        }
        return Workout.ordering.immutableSortedCopy(workouts);
    }

    public void addExercise(final UUID exerciseId, final UUID workoutId) throws Exception {
        String query = "insert into ExerciseInWorkout (workoutId, exerciseId)" +
                " values(:workoutId:, :exerciseId:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("workoutId", workoutId.toString());
        statement.setString("exerciseId", exerciseId.toString());
        statement.getStatement().executeUpdate();
    }

    public void addExerciseWithEquipment(final UUID exerciseId, final UUID workoutId) throws Exception {
        String query = "insert into ExerciseWithEquipmentInWorkout (workoutId, exerciseId)" +
                " values(:workoutId:, :exerciseId:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("workoutId", workoutId.toString());
        statement.setString("exerciseId", exerciseId.toString());
        statement.getStatement().executeUpdate();
    }

    public void create(final Workout workout) throws Exception {
        String query = "insert into Workout (workoutId, performance, personalShape, note, startTime, endTime)" +
                " values (:workoutId:, :performance:, :personalShape:, :note:, :startTime:, :endTime:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("workoutId", workout.getWorkoutId().toString());
        statement.setInt("performance", workout.getPerformance());
        statement.setInt("personalShape", workout.getPersonalShape());
        statement.setString("note", workout.getNote());
        statement.setTimestamp("startTime", workout.getStartTime());
        statement.setTimestamp("endTime", workout.getEndTime());
        statement.getStatement().executeUpdate();

        workout.getExercises().stream()
                .filter(exercise -> uncheckCall(() ->
                        !exerciseDBManager.getById(exercise.getExerciseId()).isPresent()))
                .forEach(exercise -> uncheckRun(() -> exerciseDBManager.create(exercise)));
        workout.getExerciseWithEquipments().stream()
                .filter(exercise -> uncheckCall(() ->
                        !exerciseWithEquipmentDBManager
                                .getById(exercise.getExerciseId()).isPresent()))
                .forEach(exercise -> uncheckRun(() -> exerciseWithEquipmentDBManager
                        .create(exercise)));

        workout.getExercises().forEach(exercise -> uncheckRun(() ->
                addExercise(exercise.getExerciseId(), workout.getWorkoutId())));
        workout.getExerciseWithEquipments().forEach(exercise -> uncheckRun(() ->
                addExerciseWithEquipment(exercise.getExerciseId(), workout.getWorkoutId())));
    }
}
