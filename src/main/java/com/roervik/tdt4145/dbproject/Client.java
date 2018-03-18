package com.roervik.tdt4145.dbproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.roervik.tdt4145.dbproject.Program.*;
import static com.roervik.tdt4145.dbproject.util.StreamUtils.*;

public class Client {
    private static final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();

    private static Map<String, DBManager> dbManagers;

    private static final Consumer<Map<String, String>> listAction = arguments ->
            uncheckRun(() -> writeForEach(
                    dbManagers.get(arguments.get("object")).getAll(),
                    o -> uncheckCall(() -> mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o)),
                    arguments
            ));

    private static final Consumer<Map<String, String>> createAction = arguments ->
            uncheckRun(() -> dbManagers.get(arguments.get("object")).create(
                    mapper.readValue(readFile(arguments.get("input")),
                    Class.forName("com.roervik.tdt4145.dbproject.model." + arguments.get("object")))));

    private static final Map<String, Consumer<Map<String, String>>> actions =
            ImmutableMap.of(
                    "list", listAction,
                    "create", createAction);

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
        actions.get(args[0]).accept(arguments);
        Program.closeConnections();
        System.out.println("Done");
    }

    private static <T> void writeForEach(final Iterable<T> iterable,
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
