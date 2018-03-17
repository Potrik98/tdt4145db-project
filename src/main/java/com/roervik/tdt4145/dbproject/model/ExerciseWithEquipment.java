package com.roervik.tdt4145.dbproject.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class ExerciseWithEquipment {
    final UUID exerciseId;
    final String description;
    final Equipment equipment;
}
