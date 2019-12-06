package example;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.sql.SQLException;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface IWebService {
    @WebMethod
    String readClient(Integer id) throws ClassNotFoundException;
    @WebMethod
    boolean deleteClient(Integer id) throws ClassNotFoundException, SQLException;
    @WebMethod
    boolean createClient( Client client) throws ClassNotFoundException;
    @WebMethod
    boolean updateClient(Client client) throws ClassNotFoundException;
}
