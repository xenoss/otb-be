package amqasd;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import lombok.val;
import org.jboss.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(name = "MessageQueue", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/MessageQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class MessageQueueMDB implements MessageListener {
    private final static Logger log = Logger.getLogger(MessageQueueMDB.class.toString());
    private AerospikeClient aerospikeClient;

    @Override
    public void onMessage(Message rcvMessage) {
        try {
            if (rcvMessage instanceof TextMessage) {
                val msg = (TextMessage) rcvMessage;
                log.info("Received Message from queue: " + msg.getText());

                val message = api.Message.fromString(msg.getText());
                sendToAerospike(message);
            } else {
                log.warn("Message of wrong type: " + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendToAerospike(api.Message message) {
        log.info("sendToAerospike message=" + message);

        if (aerospikeClient == null) {
            synchronized (this) {
                if (aerospikeClient == null) {
                    aerospikeClient = new AerospikeClient(null, "localhost", 3000);
                }
            }
        }

        aerospikeClient.put(null, new Key("namespace", "set", message.getId()),
                new Bin("sender", message.getSender()),
                new Bin("receiver", message.getReceiver()),
                new Bin("text", message.getText()),
                new Bin("timestamp", message.getTimestamp())
        );
    }
}
