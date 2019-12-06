package client;
import example.IWebService;
import example.MessageReceiver;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.jms.JMSException;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class ClientApp implements Runnable {
    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://localhost:1986/wss/hello?wsdl");
        QName qname = new QName("http://example/", "WebServiceImplService");
        Service service = Service.create(url, qname);
        IWebService soapWebService = service.getPort(IWebService.class);
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(new ClientApp());
        try {
            System.out.println(soapWebService.readClient(3).toString());
            System.out.println(soapWebService.readClient(4).toString());
            System.out.println(soapWebService.readClient(7).toString());
            System.out.println(soapWebService.readClient(9).toString());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        MessageReceiver mr  = null;
        try {
            while(true) {
                //забрать сообщения из очереди
                mr = new MessageReceiver();
                mr.receiveMessage();
                mr.close();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
