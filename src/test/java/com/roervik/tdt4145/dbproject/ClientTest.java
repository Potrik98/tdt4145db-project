package com.roervik.tdt4145.dbproject;

import org.junit.Test;

public class ClientTest {
    @Test
    public void testLoadJSONObject() throws Exception {
        Program.init();
        Program.exerciseDBManager.loadCreateScript();
        final String[] args = {"create", "object=Exercise", "input=testExercise.json"};
        Client.main(args);
        Program.closeConnections();
    }
}
