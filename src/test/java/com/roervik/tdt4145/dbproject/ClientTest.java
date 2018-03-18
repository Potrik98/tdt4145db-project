package com.roervik.tdt4145.dbproject;

import com.google.common.collect.ImmutableList;
import com.roervik.tdt4145.dbproject.model.Exercise;
import com.roervik.tdt4145.dbproject.model.Workout;
import org.junit.Test;

import java.time.LocalDateTime;

import static com.roervik.tdt4145.dbproject.Program.exerciseDBManager;
import static com.roervik.tdt4145.dbproject.Program.workoutDBManager;

public class ClientTest {
    @Test
    public void testLoadJSONObject() throws Exception {
        Program.init();
        exerciseDBManager.loadCreateScript();
        final String[] args = {"create", "object=Exercise", "input=testExercise.json"};
        Client.main(args);
        Program.closeConnections();
    }

    @Test
    public void testPrintResults() throws Exception {
        Program.init();
        exerciseDBManager.loadCreateScript();
        final Exercise exercise = Exercise.builder()
                .name("name")
                .description("desc")
                .build();
        exerciseDBManager.create(exercise);
        final Workout workout1 = Workout.builder()
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .exercises(ImmutableList.of(exercise))
                .exerciseWithEquipments(ImmutableList.of())
                .performance(10)
                .personalShape(10)
                .build();
        workoutDBManager.create(workout1);
        final Workout workout2 = Workout.builder()
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .exercises(ImmutableList.of(exercise))
                .exerciseWithEquipments(ImmutableList.of())
                .performance(10)
                .personalShape(10)
                .build();
        workoutDBManager.create(workout2);
        final String[] args = {"results", "object=Exercise"};
        Client.main(args);
        Program.closeConnections();
    }
}
