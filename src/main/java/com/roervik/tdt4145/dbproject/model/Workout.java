package com.roervik.tdt4145.dbproject.model;

import com.google.common.collect.Ordering;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class Workout {
    public static final Ordering<Workout> ordering = Ordering.from(Comparator.comparing(Workout::getEndTime));

    final UUID workoutId;
    final int performance;
    final int personalShape;
    final LocalDateTime startTime;
    final LocalDateTime endTime;
    final List<Exercise> exercises;
    final List<ExerciseWithEquipment> exercisesWithEquipment;
}
