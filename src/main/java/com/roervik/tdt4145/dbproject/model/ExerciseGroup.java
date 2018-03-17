package com.roervik.tdt4145.dbproject.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder(toBuilder = true)
public class ExerciseGroup {
    final int groupId;
    final String name;
    final List<Exercise> exercises;
    final List<ExerciseWithEquipment> exercisesWithEquipment;
}
