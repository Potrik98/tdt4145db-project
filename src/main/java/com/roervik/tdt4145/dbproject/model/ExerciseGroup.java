package com.roervik.tdt4145.dbproject.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.Ordering;
import lombok.Builder;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@JsonDeserialize(builder = ExerciseGroup.ExerciseGroupBuilder.class)
public class ExerciseGroup {
    public static final Ordering<ExerciseGroup> ordering =
            Ordering.from(Comparator.comparing(ExerciseGroup::getGroupId));

    final UUID groupId;
    final String name;
    final List<Exercise> exercises;
    final List<ExerciseWithEquipment> exerciseWithEquipments;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class ExerciseGroupBuilder {
        public ExerciseGroupBuilder() {
            this.groupId = UUID.randomUUID();
        }
    }
}
