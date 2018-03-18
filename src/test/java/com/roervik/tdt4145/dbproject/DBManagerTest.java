package com.roervik.tdt4145.dbproject;

import com.roervik.tdt4145.dbproject.DBManager;
import com.roervik.tdt4145.dbproject.dbmanager.WorkoutDBManager;
import org.junit.Test;

public class DBManagerTest {
    @Test
    public void testOpenDBConnectionAndLoadCreateScript() throws Exception {
        final DBManager dbManager = new WorkoutDBManager();
        dbManager.loadCreateScript();
        dbManager.closeConnection();
    }
}
