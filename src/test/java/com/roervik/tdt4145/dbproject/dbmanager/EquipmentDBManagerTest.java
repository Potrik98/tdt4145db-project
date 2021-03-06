package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.Program;
import com.roervik.tdt4145.dbproject.model.Equipment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static com.roervik.tdt4145.dbproject.Program.equipmentDBManager;
import static org.assertj.core.api.Assertions.assertThat;

public class EquipmentDBManagerTest {
    @Before
    public void openDBManager() throws Exception {
        Program.init();
        equipmentDBManager.loadCreateScript();
    }

    @After
    public void closeDBManager() throws Exception {
        Program.closeConnections();
    }

    @Test
    public void testCreateAndGetEquipment() throws Exception {
        final Equipment equipment = Equipment.builder()
                .description("MyDescription")
                .name("EquipmentName")
                .build();
        equipmentDBManager.create(equipment);

        final Optional<Equipment> retrievedEquipment =
                equipmentDBManager.getById(equipment.getEquipmentId());
        assertThat(retrievedEquipment).isPresent();
        assertThat(retrievedEquipment.get())
                .isEqualToComparingFieldByField(equipment);
    }

    @Test
    public void testGetEquipmentWithInvalidId() throws Exception {
        final UUID invalidId = UUID.randomUUID();
        final Optional<Equipment> retrievedEquipment =
                equipmentDBManager.getById(invalidId);
        assertThat(retrievedEquipment).isEmpty();
    }
}
