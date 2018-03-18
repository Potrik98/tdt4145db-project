package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.Program;
import com.roervik.tdt4145.dbproject.model.Equipment;
import com.roervik.tdt4145.dbproject.model.ExerciseWithEquipment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static com.roervik.tdt4145.dbproject.Program.equipmentDBManager;
import static com.roervik.tdt4145.dbproject.Program.exerciseWithEquipmentDBManager;
import static org.assertj.core.api.Assertions.assertThat;

public class ExerciseWithEquipmentDBManagerTest {
    @Before
    public void openDBManager() throws Exception {
        Program.init();
        exerciseWithEquipmentDBManager.loadCreateScript();
    }

    @After
    public void closeDBManager() throws Exception {
        Program.closeConnections();
    }

    @Test
    public void testCreateAndGetExerciseWithoutExistingEquipment() throws Exception {
        final Equipment equipment = Equipment.builder()
                .description("MyDescription")
                .name("EquipmentName")
                .build();

        final ExerciseWithEquipment exerciseWithEquipment = ExerciseWithEquipment.builder()
                .description("MyDescription")
                .equipment(equipment)
                .build();
        exerciseWithEquipmentDBManager.create(exerciseWithEquipment);

        final Optional<ExerciseWithEquipment> retrievedExerciseWithEquipment =
                exerciseWithEquipmentDBManager.getById(exerciseWithEquipment.getExerciseId());
        assertThat(retrievedExerciseWithEquipment).isPresent();
        assertThat(retrievedExerciseWithEquipment.get())
                .isEqualToComparingFieldByFieldRecursively(exerciseWithEquipment);
    }

    @Test
    public void testCreateAndGetExerciseWithEquipment() throws Exception {
        final Equipment equipment = Equipment.builder()
                .description("MyDescription")
                .name("EquipmentName")
                .build();
        equipmentDBManager.create(equipment);

        final ExerciseWithEquipment exerciseWithEquipment = ExerciseWithEquipment.builder()
                .description("MyDescription")
                .equipment(equipment)
                .build();
        exerciseWithEquipmentDBManager.create(exerciseWithEquipment);

        final Optional<ExerciseWithEquipment> retrievedExerciseWithEquipment =
                exerciseWithEquipmentDBManager.getById(exerciseWithEquipment.getExerciseId());
        assertThat(retrievedExerciseWithEquipment).isPresent();
        assertThat(retrievedExerciseWithEquipment.get())
                .isEqualToComparingFieldByFieldRecursively(exerciseWithEquipment);
    }

    @Test
    public void testGetExerciseWithEquipmentWithInvalidId() throws Exception {
        final UUID invalidId = UUID.randomUUID();
        final Optional<ExerciseWithEquipment> retrievedExerciseWithEquipment =
                exerciseWithEquipmentDBManager.getById(invalidId);
        assertThat(retrievedExerciseWithEquipment).isEmpty();
    }
}
