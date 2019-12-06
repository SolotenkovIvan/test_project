package example;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MessageReceiver {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    // default broker URL is : tcp://localhost:61616"
    private static String subject = "MESSAGE";
    private Connection connection;
    private MessageConsumer consumer;

    public MessageReceiver() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(subject);
        consumer = session.createConsumer(destination);
    }

    public String receiveMessage() throws JMSException {
        Message message = consumer.receive();
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("Received message '" + textMessage.getText() + "'");
            return textMessage.getText();
        }
        return null;
    }
    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }
}
