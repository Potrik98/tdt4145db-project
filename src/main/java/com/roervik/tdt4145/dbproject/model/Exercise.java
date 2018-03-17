package com.roervik.tdt4145.dbproject.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class Exercise {
    final UUID exerciseId;
    final String name;
    final String description;
}
