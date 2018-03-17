import com.roervik.tdt4145.dbproject.DBConnection;
import org.junit.Test;

public class DBConnectionTest {
    @Test
    public void testOpenDBConnectionAndLoadCreateScript() throws Exception {
        DBConnection dbConnection = new DBConnection("dbconnection.properties");
        dbConnection.loadCreateScript();
        dbConnection.closeConnection();
    }
}
