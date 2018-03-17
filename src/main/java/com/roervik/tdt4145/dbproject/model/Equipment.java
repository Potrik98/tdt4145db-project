package com.roervik.tdt4145.dbproject.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class Equipment {
    final UUID equipmentId;
    final String name;
    final String description;
}
