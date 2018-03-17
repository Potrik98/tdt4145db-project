package com.roervik.tdt4145.dbproject.util;

import java.util.concurrent.Callable;

public class StreamUtils {
    public static <T> T uncheckCall(Callable<T> callable) {
        try { return callable.call(); }
        catch (RuntimeException e) { throw e; }
        catch (Exception e) { throw new RuntimeException(e); }
    }
}
