package com.roervik.tdt4145.dbproject.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ExerciseWithEquipment {
    final int exerciseId;
    final String description;
    final Equipment equipment;
}
