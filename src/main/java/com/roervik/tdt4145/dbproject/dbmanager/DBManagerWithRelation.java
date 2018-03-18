package com.roervik.tdt4145.dbproject.dbmanager;

import java.util.UUID;

public abstract class DBManagerWithRelation<T> extends DBManager<T> {
    public DBManagerWithRelation() throws Exception {
        super();
    }

    public abstract void addExercise(final UUID exerciseId, final UUID objectId) throws Exception;

    public abstract void addExerciseWithEquipment(
            final UUID exerciseId, final UUID objectId) throws Exception;
}
