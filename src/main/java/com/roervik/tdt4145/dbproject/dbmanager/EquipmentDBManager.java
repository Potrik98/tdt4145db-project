package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.DBConnection;
import com.roervik.tdt4145.dbproject.model.Equipment;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;

public class EquipmentDBManager extends DBConnection {
    public EquipmentDBManager() throws Exception {
        super();
    }

    public Optional<Equipment> getEquipmentById(final UUID equipmentId) throws Exception {
        final String query = "select name, description from Equipment" +
                " where equipmentId = :equipmentId:";
        final NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("equipmentId", equipmentId.toString());
        final ResultSet result = statement.getStatement().executeQuery();
        if(!result.first()) {
            return Optional.empty();
        }
        final Equipment equipment = Equipment.builder()
                .equipmentId(equipmentId)
                .name(result.getString("name"))
                .description(result.getString("description"))
                .build();
        return Optional.of(equipment);
    }

    public void createEquipment(final Equipment equipment) throws Exception {
        String query = "insert into Equipment (equipmentId, name, description)" +
                " values (:equipmentId:, :name:, :description:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("equipmentId", equipment.getEquipmentId().toString());
        statement.setString("name", equipment.getName());
        statement.setString("description", equipment.getDescription());
        statement.getStatement().executeUpdate();
    }
}
