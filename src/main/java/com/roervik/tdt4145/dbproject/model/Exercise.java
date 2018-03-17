package com.roervik.tdt4145.dbproject.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Exercise {
    final int exerciseId;
    final String name;
    final String description;
}
