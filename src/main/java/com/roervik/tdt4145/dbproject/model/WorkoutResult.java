package com.roervik.tdt4145.dbproject.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder(toBuilder = true)
public class WorkoutResult {
    final double averagePerformance;
    final double averagePersonalShape;
    final List<Integer> performances;
    final List<Integer> personalShapes;
    final LocalDateTime startTime;
    final LocalDateTime endTime;

    public static WorkoutResult ofWorkouts(final List<Workout> workouts) {
        return WorkoutResult.builder()
                .averagePerformance(workouts.stream()
                        .mapToInt(Workout::getPerformance)
                        .average()
                        .getAsDouble())
                .averagePersonalShape(
                        workouts.stream()
                                .mapToInt(Workout::getPerformance)
                                .average()
                                .getAsDouble())
                .performances(workouts.stream()
                        .map(Workout::getPerformance)
                        .collect(Collectors.toList()))
                .personalShapes(workouts.stream()
                        .map(Workout::getPersonalShape)
                        .collect(Collectors.toList()))
                .startTime(workouts.stream()
                        .map(Workout::getStartTime)
                        .min(LocalDateTime::compareTo)
                        .get())
                .endTime(workouts.stream()
                        .map(Workout::getEndTime)
                        .max(LocalDateTime::compareTo)
                        .get())
                .build();
    }
}
