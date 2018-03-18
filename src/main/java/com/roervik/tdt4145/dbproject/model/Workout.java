package com.roervik.tdt4145.dbproject.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.Ordering;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@JsonDeserialize(builder = Workout.WorkoutBuilder.class)
public class Workout {
    public static final Ordering<Workout> ordering = Ordering.from(Comparator.comparing(Workout::getEndTime));

    final UUID workoutId;
    final Integer performance;
    final Integer personalShape;
    final LocalDateTime startTime;
    final LocalDateTime endTime;
    final List<Exercise> exercises;
    final List<ExerciseWithEquipment> exerciseWithEquipments;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class WorkoutBuilder {
        public WorkoutBuilder() {
            this.workoutId = UUID.randomUUID();
        }
    }
}
