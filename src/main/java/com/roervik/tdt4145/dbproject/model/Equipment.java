package com.roervik.tdt4145.dbproject.model;

import com.google.common.collect.Ordering;
import lombok.Builder;
import lombok.Data;

import java.util.Comparator;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class Equipment {
    public static final Ordering<Equipment> ordering = Ordering.from(Comparator.comparing(Equipment::getEquipmentId));

    final UUID equipmentId;
    final String name;
    final String description;
}
