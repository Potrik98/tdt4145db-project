package com.roervik.tdt4145.dbproject.model;

import lombok.Builder;

@Builder(toBuilder = true)
public class ExerciseWithEquipment {
    final int exerciseId;
    final String description;
    final Equipment equipment;
}
