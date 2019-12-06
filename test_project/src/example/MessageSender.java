package example;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MessageSender {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject = "MESSAGE";
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer producer;
    private TextMessage message = null;

    public MessageSender() throws JMSException {
        connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue(subject);
        producer = session.createProducer(destination);
    }

    public void sendMessage(String msg) throws JMSException {
            message = session.createTextMessage(msg);
            producer.send(message);
    }

    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }
}
