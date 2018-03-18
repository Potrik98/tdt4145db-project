package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.DBManager;
import com.roervik.tdt4145.dbproject.model.Equipment;
import com.roervik.tdt4145.dbproject.util.NamedParameterStatement;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EquipmentDBManager extends DBManager<Equipment> {
    public EquipmentDBManager() throws Exception {
        super();
    }

    public List<Equipment> getAll() throws Exception {
        final String query = "select equipmentId, equipmentName, description from Equipment;";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        final ResultSet result = statement.getStatement().executeQuery();
        final List<Equipment> equipments = new ArrayList<>();
        while(result.next()) {
            final Equipment equipment = Equipment.builder()
                    .equipmentId(UUID.fromString(result.getString("equipmentId")))
                    .name(result.getString("equipmentName"))
                    .description(result.getString("description"))
                    .build();
            equipments.add(equipment);
        }
        return Equipment.ordering.immutableSortedCopy(equipments);
    }

    public Optional<Equipment> getById(final UUID equipmentId) throws Exception {
        final String query = "select equipmentName, description from Equipment" +
                " where equipmentId = :equipmentId:";
        final NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("equipmentId", equipmentId.toString());
        final ResultSet result = statement.getStatement().executeQuery();
        if(!result.first()) {
            return Optional.empty();
        }
        final Equipment equipment = Equipment.builder()
                .equipmentId(equipmentId)
                .name(result.getString("equipmentName"))
                .description(result.getString("description"))
                .build();
        return Optional.of(equipment);
    }

    public void create(final Equipment equipment) throws Exception {
        String query = "insert into Equipment (equipmentId, equipmentName, description)" +
                " values (:equipmentId:, :equipmentName:, :description:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, connection);
        statement.setString("equipmentId", equipment.getEquipmentId().toString());
        statement.setString("equipmentName", equipment.getName());
        statement.setString("description", equipment.getDescription());
        statement.getStatement().executeUpdate();
    }
}
