package com.roervik.tdt4145.dbproject.model;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public class ExerciseGroup {
    final int groupId;
    final String name;
    final List<Exercise> exercises;
    final List<ExerciseWithEquipment> exercisesWithEquipment;
}
