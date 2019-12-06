import example.Client;
import example.WebServiceImpl;
import org.junit.*;
import java.sql.SQLException;

public class TestWebServiceImpl {
    private static WebServiceImpl ws;
    @BeforeClass
    public static void runT()  {
        ws = new WebServiceImpl();
    }


    @Test
    public void testCreateClient() {
        try {
            boolean result = ws.createClient(new Client(50,
                                                        "Егор",
                                                        "Синицын",
                                                        "14.05.1963",
                                                        "+79185551210",
                                                        "sinicin@mail.ru",
                                                        23.54));
            Assert.assertTrue(result);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadClient() {
        String equalsResult = "Client{id=3, firstname='Аскар', lastname='Истамов', birthdate='1984-05-21', phone='+79185551219', email='istamov@mail.ru', balance=42.5}";
        String result = null;
        try {
            result = ws.readClient(3);
            Assert.assertEquals(result, equalsResult);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testUpdateClient() {
        Client updClient = null;
        boolean result = false;
        try {
            updClient = new Client(5, "Иван","Солотенков", "16.07.1993", "+70000000000","solotenkov@mail.ru", 12.3);
            result = ws.updateClient(updClient);
            Assert.assertTrue(result);
        } catch (ClassNotFoundException e) {
            e.getMessage();
        }
    }

    @Test
    public void testDeleteClient() {
        try {
            boolean result = ws.deleteClient(7);
            Assert.assertTrue(result);
        } catch (ClassNotFoundException | SQLException e) {
            e.getMessage();
        }
    }
}
