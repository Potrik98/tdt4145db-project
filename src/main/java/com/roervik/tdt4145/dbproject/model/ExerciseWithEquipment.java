package com.roervik.tdt4145.dbproject.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.Ordering;
import lombok.Builder;
import lombok.Data;

import java.util.Comparator;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@JsonDeserialize(builder = ExerciseWithEquipment.ExerciseWithEquipmentBuilder.class)
public class ExerciseWithEquipment {
    public static final Ordering<ExerciseWithEquipment> ordering =
            Ordering.from(Comparator.comparing(ExerciseWithEquipment::getExerciseId));

    final UUID exerciseId;
    final String description;
    final Equipment equipment;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class ExerciseWithEquipmentBuilder {
        public ExerciseWithEquipmentBuilder() {
            this.exerciseId = UUID.randomUUID();
        }
    }
}
