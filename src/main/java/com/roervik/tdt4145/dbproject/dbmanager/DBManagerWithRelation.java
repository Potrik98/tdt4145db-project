package com.roervik.tdt4145.dbproject.dbmanager;

import com.roervik.tdt4145.dbproject.model.Exercise;
import com.roervik.tdt4145.dbproject.model.ExerciseWithEquipment;

import java.util.List;
import java.util.UUID;

public abstract class DBManagerWithRelation<T> extends DBManager<T> {
    public DBManagerWithRelation() throws Exception {
        super();
    }

    public abstract void addExercise(final UUID exerciseId, final UUID objectId) throws Exception;

    public abstract void addExerciseWithEquipment(
            final UUID exerciseId, final UUID objectId) throws Exception;

    public abstract List<Exercise> getRelatedExercises(final UUID objectId) throws Exception;

    public abstract List<ExerciseWithEquipment> getRelatedExerciseWithEquipments(
            final UUID objectId) throws Exception;
}
