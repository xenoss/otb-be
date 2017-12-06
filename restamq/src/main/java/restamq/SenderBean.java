package restamq;

import api.Message;
import org.jboss.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.Queue;

import static javax.jms.DeliveryMode.NON_PERSISTENT;

@JMSDestinationDefinitions(
        value = {
                @JMSDestinationDefinition(
                        name = "java:/queue/MessageQueue",
                        interfaceName = "javax.jms.Queue",
                        destinationName = "MessageQueue"
                )
        })
@Singleton
public class SenderBean implements Sender {
    private final static Logger log = Logger.getLogger(SenderBean.class.getName());

    @Inject
    private JMSContext context;

    @Resource(lookup = "java:/queue/MessageQueue")
    private Queue queue;

    @Override
    public void send(Message message) {
        log.info("[" + queue.toString() + "]send " + message);

        context.createProducer()
                .setDeliveryMode(NON_PERSISTENT)
                .setTimeToLive(100 * 1000) // 100 secs
                .send(queue, message.toJson());
    }
}
