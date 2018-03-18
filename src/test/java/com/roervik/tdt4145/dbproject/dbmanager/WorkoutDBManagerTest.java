package com.roervik.tdt4145.dbproject.dbmanager;

import com.google.common.collect.ImmutableList;
import com.roervik.tdt4145.dbproject.Program;
import com.roervik.tdt4145.dbproject.model.Equipment;
import com.roervik.tdt4145.dbproject.model.Exercise;
import com.roervik.tdt4145.dbproject.model.ExerciseWithEquipment;
import com.roervik.tdt4145.dbproject.model.Workout;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.roervik.tdt4145.dbproject.Program.*;
import static org.assertj.core.api.Assertions.assertThat;

public class WorkoutDBManagerTest {
    @Before
    public void openDBManager() throws Exception {
        Program.init();
        workoutDBManager.loadCreateScript();
    }

    @After
    public void closeDBManager() throws Exception {
        Program.closeConnections();
    }

    @Test
    public void testCreateAndGetWorkoutWithExercises() throws Exception {
        final Equipment equipment = Equipment.builder()
                .description("MyDescription")
                .name("EquipmentName")
                .build();
        final ExerciseWithEquipment createdExerciseWithEquiment = ExerciseWithEquipment.builder()
                .description("MyDescription")
                .equipment(equipment)
                .build();
        exerciseWithEquipmentDBManager.create(createdExerciseWithEquiment);
        final ExerciseWithEquipment notCreatedExerciseWithEquipment = ExerciseWithEquipment.builder()
                .description("MyDescription")
                .equipment(equipment)
                .build();
        final Exercise createdExercise = Exercise.builder()
                .description("MyDescription")
                .name("ExerciseName")
                .build();
        exerciseDBManager.create(createdExercise);
        final Exercise notCreatedExercise = Exercise.builder()
                .description("MyDescription")
                .name("ExerciseName")
                .build();
        final Workout workout = Workout.builder()
                .performance(123)
                .personalShape(123)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .exercises(Exercise.ordering.immutableSortedCopy(
                        ImmutableList.of(createdExercise, notCreatedExercise)))
                .exerciseWithEquipments(ExerciseWithEquipment.ordering.immutableSortedCopy(
                        ImmutableList.of(createdExerciseWithEquiment, notCreatedExerciseWithEquipment)))
                .build();
        workoutDBManager.create(workout);

        final Optional<Workout> retrievedWorkout =
                workoutDBManager.getById(workout.getWorkoutId());
        assertThat(retrievedWorkout).isPresent();
        assertThat(retrievedWorkout.get()).isEqualToComparingFieldByFieldRecursively(workout);
    }

    @Test
    public void testGetWorkoutWithInvalidId() throws Exception {
        final UUID invalidId = UUID.randomUUID();
        final Optional<Workout> retrievedWorkout =
                workoutDBManager.getById(invalidId);
        assertThat(retrievedWorkout).isEmpty();
    }
}
