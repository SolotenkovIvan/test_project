package example;

import javax.jms.JMSException;
import javax.jws.WebService;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

@WebService(endpointInterface = "example.IWebService")
public class WebServiceImpl implements IWebService {
    private Config config;
    public WebServiceImpl() {
        config = new Config();
    }

    @Override
    public String readClient(Integer id) throws ClassNotFoundException {
        Class.forName(config.driverName);
        Client result = null;
        try (Connection conn = DriverManager.getConnection(config.connectionURL, config.userName, config.password);
             Statement statement = conn.createStatement()) {
            MessageSender ms = new MessageSender();
            ResultSet resultSet = statement.executeQuery("select * from client where id = " + id);
                resultSet.next();
                result = new Client(resultSet.getInt(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4),
                                    resultSet.getString(5),
                                    resultSet.getString(6),
                                    resultSet.getDouble(7)
                                    );
            ms.sendMessage((id + ":" + OperationType.READ));
            ms.close();
            return result.toString();
        } catch (SQLException | JMSException e) {
            e.getMessage();
        }
        return null;
    }

    @Override
    public boolean deleteClient(Integer id) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM Client WHERE ID = " + id;
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            MessageSender ms = new MessageSender();
            conn = DriverManager.getConnection(config.connectionURL, config.userName, config.password);
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(sql);
            stat.execute();
            System.out.println("Client is deleted from table");
            conn.commit();
            ms.sendMessage((id + ":" + OperationType.DELETE));
            ms.close();
        } catch (SQLException e) {
            assert conn != null;
            conn.rollback();
            e.getMessage();
        } catch (JMSException e) {
            e.getMessage();
        } finally {
            if (stat != null) stat.close();
            if (conn != null) conn.close();
        }
        return true;
    }

    @Override
    public boolean createClient(Client client) throws ClassNotFoundException {
        Class.forName(config.driverName);
        String sql = "INSERT INTO Client (id, firstname, lastname, birthdate, phone, email, balance) values  (?,?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(config.connectionURL, config.userName, config.password);
             PreparedStatement stat = conn.prepareStatement(sql)) {
            MessageSender ms = new MessageSender();
            stat.setString(1, client.getId().toString());
            stat.setString(2, client.getFirstname());
            stat.setString(3, client.getLastname());
            DateFormat dateFormat = DateFormat.getDateInstance();
            Date date = dateFormat.parse(String.valueOf(client.getBirthdate()));
            stat.setDate(4, new java.sql.Date(date.getTime()));
            stat.setString(5, client.getPhone());
            stat.setString(6, client.getEmail());
            stat.setString(7, String.valueOf(client.getBalance()));
            stat.executeUpdate();
            ms.sendMessage((client.getId() + ":" + OperationType.CREATE));
            ms.close();
        } catch (SQLException e) {
            e.getMessage();
            return false;
        } catch (ParseException e) {
            e.getMessage();
            return false;
        } catch (JMSException e) {
            e.getMessage();
        }
        return true;
    }

    @Override
    public boolean updateClient(Client client) throws ClassNotFoundException {
        Class.forName(config.driverName);
        String sql = "update client set firstname=?, lastname=?, birthdate=?, phone=?, email = ?, balance = ? where id=?";
        try (Connection conn = DriverManager.getConnection(config.connectionURL, config.userName, config.password);
             PreparedStatement stat = conn.prepareStatement(sql)) {
            MessageSender ms = new MessageSender();
            stat.setString(1, client.getFirstname());
            stat.setString(2, client.getLastname());
            DateFormat dateFormat = DateFormat.getDateInstance();
            Date date = dateFormat.parse(String.valueOf(client.getBirthdate()));
            stat.setDate(3, new java.sql.Date(date.getTime()));
            stat.setString(4, client.getPhone());
            stat.setString(5, client.getEmail());
            stat.setString(6, String.valueOf(client.getBalance()));
            stat.setString(7, client.getId().toString());
            stat.executeUpdate();
            ms.sendMessage((client.getId() + ":" + OperationType.UPDATE));
            ms.close();
        } catch (SQLException e) {
            e.getMessage();
            return false;
        } catch (ParseException | JMSException e) {
            e.getMessage();
        }
        return true;
    }
}

