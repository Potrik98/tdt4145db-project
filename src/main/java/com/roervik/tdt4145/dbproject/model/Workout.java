package com.roervik.tdt4145.dbproject.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class Workout {
    final int workoutId;
    final int performance;
    final int personalShape;
    final LocalDateTime startTime;
    final LocalDateTime endTime;
    final List<Exercise> exercises;
    final List<ExerciseWithEquipment> exercisesWithEquipment;
}
