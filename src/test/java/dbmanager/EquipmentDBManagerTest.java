package dbmanager;

import com.roervik.tdt4145.dbproject.dbmanager.EquipmentDBManager;
import com.roervik.tdt4145.dbproject.model.Equipment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EquipmentDBManagerTest {
    private EquipmentDBManager equipmentDBManager;

    @Before
    public void openDBManager() throws Exception {
        equipmentDBManager = new EquipmentDBManager();
        equipmentDBManager.loadCreateScript();
    }

    @After
    public void closeDBManager() throws Exception {
        equipmentDBManager.closeConnection();
    }

    @Test
    public void testCreateAndGetEquipment() throws Exception {
        final Equipment equipment = Equipment.builder()
                .equipmentId(UUID.randomUUID())
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
