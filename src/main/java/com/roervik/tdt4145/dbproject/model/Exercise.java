package com.roervik.tdt4145.dbproject.model;

import com.google.common.collect.Ordering;
import lombok.Builder;
import lombok.Data;

import java.util.Comparator;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class Exercise {
    public static final Ordering<Exercise> ordering = Ordering.from(Comparator.comparing(Exercise::getExerciseId));

    @Builder.Default
    final UUID exerciseId = UUID.randomUUID();
    final String name;
    final String description;
}
