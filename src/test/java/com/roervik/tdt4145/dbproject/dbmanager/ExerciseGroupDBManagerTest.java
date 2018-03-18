package com.roervik.tdt4145.dbproject.dbmanager;

import com.google.common.collect.ImmutableList;
import com.roervik.tdt4145.dbproject.Program;
import com.roervik.tdt4145.dbproject.model.Equipment;
import com.roervik.tdt4145.dbproject.model.Exercise;
import com.roervik.tdt4145.dbproject.model.ExerciseWithEquipment;
import com.roervik.tdt4145.dbproject.model.ExerciseGroup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static com.roervik.tdt4145.dbproject.Program.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ExerciseGroupDBManagerTest {
    @Before
    public void openDBManager() throws Exception {
        Program.init();
        exerciseGroupDBManager.loadCreateScript();
    }

    @After
    public void closeDBManager() throws Exception {
        Program.closeConnections();
    }

    @Test
    public void testCreateAndGetExerciseGroupWithExercises() throws Exception {
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
        final ExerciseGroup exerciseGroup = ExerciseGroup.builder()
                .name("GroupName")
                .exercises(Exercise.ordering.immutableSortedCopy(
                        ImmutableList.of(createdExercise, notCreatedExercise)))
                .exerciseWithEquipments(ExerciseWithEquipment.ordering.immutableSortedCopy(
                        ImmutableList.of(createdExerciseWithEquiment, notCreatedExerciseWithEquipment)))
                .build();
        exerciseGroupDBManager.create(exerciseGroup);

        final Optional<ExerciseGroup> retrievedExerciseGroup =
                exerciseGroupDBManager.getById(exerciseGroup.getGroupId());
        assertThat(retrievedExerciseGroup).isPresent();
        assertThat(retrievedExerciseGroup.get()).isEqualToComparingFieldByFieldRecursively(exerciseGroup);
    }

    @Test
    public void testGetExerciseGroupWithInvalidId() throws Exception {
        final UUID invalidId = UUID.randomUUID();
        final Optional<ExerciseGroup> retrievedExerciseGroup =
                exerciseGroupDBManager.getById(invalidId);
        assertThat(retrievedExerciseGroup).isEmpty();
    }
}
