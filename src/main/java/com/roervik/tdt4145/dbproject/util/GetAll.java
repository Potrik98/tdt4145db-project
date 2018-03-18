package com.roervik.tdt4145.dbproject.util;

import java.util.List;

public interface GetAll<T> {
    public List<T> getAll() throws Exception;
}
