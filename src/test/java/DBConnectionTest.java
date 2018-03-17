import com.roervik.tdt4145.dbproject.DBConnection;
import org.junit.Test;

public class DBConnectionTest {
    @Test
    public void testOpenDBConnectionAndLoadCreateScript() throws Exception {
        final DBConnection dbConnection = new DBConnection();
        dbConnection.loadCreateScript();
        dbConnection.closeConnection();
    }
}
