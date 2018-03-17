package dbmanager;

import com.google.common.collect.ImmutableList;
import com.roervik.tdt4145.dbproject.Program;
import com.roervik.tdt4145.dbproject.model.Equipment;
import com.roervik.tdt4145.dbproject.model.Exercise;
import com.roervik.tdt4145.dbproject.model.ExerciseWithEquipment;
import com.roervik.tdt4145.dbproject.model.ExerciseGroup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
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
                .equipmentId(UUID.randomUUID())
                .description("MyDescription")
                .name("EquipmentName")
                .build();
        final ExerciseWithEquipment createdExerciseWithEquiment = ExerciseWithEquipment.builder()
                .exerciseId(UUID.randomUUID())
                .description("MyDescription")
                .equipment(equipment)
                .build();
        exerciseWithEquipmentDBManager.createExerciseWithEquipment(createdExerciseWithEquiment);
        final ExerciseWithEquipment notCreatedExerciseWithEquipment = ExerciseWithEquipment.builder()
                .exerciseId(UUID.randomUUID())
                .description("MyDescription")
                .equipment(equipment)
                .build();
        final Exercise createdExercise = Exercise.builder()
                .exerciseId(UUID.randomUUID())
                .description("MyDescription")
                .name("ExerciseName")
                .build();
        exerciseDBManager.createExercise(createdExercise);
        final Exercise notCreatedExercise = Exercise.builder()
                .exerciseId(UUID.randomUUID())
                .description("MyDescription")
                .name("ExerciseName")
                .build();
        final ExerciseGroup exerciseGroup = ExerciseGroup.builder()
                .groupId(UUID.randomUUID())
                .name("GroupName")
                .exercises(Exercise.ordering.immutableSortedCopy(
                        ImmutableList.of(createdExercise, notCreatedExercise)))
                .exercisesWithEquipment(ExerciseWithEquipment.ordering.immutableSortedCopy(
                        ImmutableList.of(createdExerciseWithEquiment, notCreatedExerciseWithEquipment)))
                .build();
        exerciseGroupDBManager.createExerciseGroup(exerciseGroup);

        final Optional<ExerciseGroup> retrievedExerciseGroup =
                exerciseGroupDBManager.getExerciseGroupById(exerciseGroup.getGroupId());
        assertThat(retrievedExerciseGroup).isPresent();
        assertThat(retrievedExerciseGroup.get()).isEqualToComparingFieldByFieldRecursively(exerciseGroup);
    }

    @Test
    public void testGetExerciseGroupWithInvalidId() throws Exception {
        final UUID invalidId = UUID.randomUUID();
        final Optional<ExerciseGroup> retrievedExerciseGroup =
                exerciseGroupDBManager.getExerciseGroupById(invalidId);
        assertThat(retrievedExerciseGroup).isEmpty();
    }
}
