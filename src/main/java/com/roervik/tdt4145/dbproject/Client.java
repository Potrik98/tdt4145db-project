package com.roervik.tdt4145.dbproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.roervik.tdt4145.dbproject.dbmanager.DBManager;
import com.roervik.tdt4145.dbproject.dbmanager.DBManagerWithRelation;
import com.roervik.tdt4145.dbproject.model.WorkoutResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.roervik.tdt4145.dbproject.Program.*;
import static com.roervik.tdt4145.dbproject.util.StreamUtils.*;

public class Client {
    private static final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();

    private static Map<String, DBManager> dbManagers;
    private static Map<String, DBManagerWithRelation> relationDBManagers;

    private static final Consumer<Map<String, String>> listRelatedAction = arguments ->
            uncheckRun(() -> writeForEach(
                    ((List<?>) relationDBManagers.get(arguments.get("in")).getClass()
                            .getMethod("getRelated" + arguments.get("object") + "s", UUID.class)
                            .invoke(relationDBManagers.get(arguments.get("in")),
                                    UUID.fromString(arguments.get("id"))
                            )
                    )
                    .stream(),
                    o -> uncheckCall(() -> mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o)),
                    arguments
            ));

    private static final Consumer<Map<String, String>> listAction = arguments ->
            uncheckRun(() -> writeForEach(
                    ((List<?>) dbManagers.get(arguments.get("object")).getAll()).stream()
                            .limit(arguments.containsKey("count")
                                    ? Integer.valueOf(arguments.get("count")) : 100),
                    o -> uncheckCall(() -> mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o)),
                    arguments
            ));

    private static final Consumer<Map<String, String>> createListAction = arguments ->
            uncheckRun(() ->
                    ((List<?>) mapper.readValue(
                            readFile(arguments.get("input")),
                            mapper.getTypeFactory().constructCollectionType(
                                    List.class,
                                    Class.forName("com.roervik.tdt4145.dbproject.model." + arguments.get("object"))
                            )
                    ))
                    .forEach(obj -> uncheckRun(() ->
                            dbManagers.get(arguments.get("object")).create(obj)
                    ))
            );

    private static final Consumer<Map<String, String>> createAction = arguments ->
            uncheckRun(() -> dbManagers.get(arguments.get("object")).create(
                    mapper.readValue(readFile(arguments.get("input")),
                    Class.forName("com.roervik.tdt4145.dbproject.model." + arguments.get("object")))));

    private static final Consumer<Map<String, String>> resultsAction = arguments ->
            uncheckRun(() -> writeForEach(
                    ((Map<?, WorkoutResult>)
                            (arguments.containsKey("id")
                                    ? Stream.of(dbManagers.get(arguments.get("object"))
                                            .getById(UUID.fromString(arguments.get("id"))))
                                    : ((List<?>) dbManagers.get(arguments.get("object")).getAll()).stream()
                            )
                            .collect(Collectors.toMap(
                                    Function.identity(),
                                    ex -> uncheckCall(() -> WorkoutResult.ofWorkouts(
                                                    workoutDBManager.getAll().stream()
                                                    .filter(workout -> !arguments.containsKey("startTime")
                                                            || LocalDateTime.parse(arguments.get("startTime"))
                                                                    .isBefore(workout.getStartTime())
                                                    )
                                                    .filter(workout -> !arguments.containsKey("endTime")
                                                            || LocalDateTime.parse(arguments.get("endTime"))
                                                                    .isAfter(workout.getEndTime())
                                                    )
                                                    .filter(workout -> uncheckCall(() ->
                                                            ((List<?>)
                                                                    workout.getClass()
                                                                            .getMethod("get" + arguments.get("object") + "s")
                                                                            .invoke(workout)
                                                            )
                                                            .stream()
                                                            .map(o -> uncheckCall(() ->
                                                                    (UUID) o.getClass().getMethod("getExerciseId")
                                                                            .invoke(o)))
                                                            .anyMatch(id -> id.equals(uncheckCall(() ->
                                                                    (UUID) ex.getClass().getMethod("getExerciseId")
                                                                            .invoke(ex)
                                                            )))
                                                    ))
                                                    .collect(Collectors.toList())
                                            )
                                    )
                            ))
                    )
                    .entrySet().stream(),
                    o -> uncheckCall(() -> mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o)),
                    arguments
            ));

    private static final Map<String, Consumer<Map<String, String>>> actions =
            ImmutableMap.of(
                    "list", listAction,
                    "create", createAction,
                    "createList", createListAction,
                    "listRelated", listRelatedAction,
                    "results", resultsAction);

    public static void main(String[] args) throws Exception {
        final Map<String, String> arguments = parseArguments(args);
        Program.init();
        System.out.println(args[0] + " object " + arguments.get("object"));
        dbManagers = ImmutableMap.of(
                "Equipment", equipmentDBManager,
                "Exercise", exerciseDBManager,
                "ExerciseWithEquipment", exerciseWithEquipmentDBManager,
                "Workout", workoutDBManager,
                "ExerciseGroup", exerciseGroupDBManager);
        relationDBManagers = ImmutableMap.of(
                "Workout", workoutDBManager,
                "ExerciseGroup", exerciseGroupDBManager);
        actions.get(args[0]).accept(arguments);
        Program.closeConnections();
        System.out.println("Done");
    }

    private static <T> void writeForEach(final Stream<T> iterable,
                                         final Function<T, String> function,
                                         final Map<String, String> arguments) {
        final PrintWriter writer = getOutputPrintWriter(arguments);
        iterable.forEach(t -> writer.println(function.apply(t)));
        writer.flush();
    }

    private static Map<String, String> parseArguments(final String[] args) {
        return Arrays.stream(args)
                .skip(1)
                .map(arg -> arg.split("="))
                .collect(Collectors.toMap(data -> data[0], data -> data[1]));
    }

    private static PrintWriter getOutputPrintWriter(final Map<String, String> arguments) {
        PrintWriter writer;
        if (arguments.containsKey("output")) {
            final String fileName = arguments.get("output");
            try {
                writer = new PrintWriter(new FileOutputStream(new File(fileName)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                writer = new PrintWriter(System.out);
            }
        } else {
            writer = new PrintWriter(System.out);
        }
        return writer;
    }
}
