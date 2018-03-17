package com.roervik.tdt4145.dbproject.model;

import lombok.Builder;

@Builder(toBuilder = true)
public class Equipment {
    final int equipmentId;
    final String name;
    final String description;
}
