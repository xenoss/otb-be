package restamq;

import api.Message;
import org.jboss.logging.Logger;
import org.jboss.msc.service.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageQueueMDB implements Service<String> {
    public static final ServiceName SINGLETON_SERVICE_NAME = ServiceName.JBOSS.append("test", "testamq");

    private final static Logger log = Logger.getLogger(MessageQueueMDB.class.getName());

    private final AtomicBoolean started = new AtomicBoolean(false);

    private Sender sender;

    private void send(Message message) {
        log.info("send " + message);

        sender.send(message);
    }

    public void run() {
        log.info("[" + System.getProperty("jboss.node.name") + "] run...");
        RestClient.get(2).forEach(this::send);
    }

    @Override
    public void start(StartContext startContext) throws StartException {
        if (!started.compareAndSet(false, true)) {
            throw new StartException("The service is still started!");
        }

        log.info("[" + System.getProperty("jboss.node.name") + "] Start MessageQueueMDB service '" + this.getClass().getName() + "'");

        try {
            InitialContext ic = new InitialContext();
            ((Scheduler) ic.lookup("global/restamq/SchedulerBean!restamq.Scheduler"))
                    .initialize(this::run);

            sender = ((Sender) ic.lookup("global/restamq/SenderBean!restamq.Sender"));
        } catch (NamingException e) {
            throw new StartException(e);
        }
    }

    @Override
    public void stop(StopContext stopContext) {
        if (!started.compareAndSet(true, false)) {
            log.warn("The service '" + this.getClass().getName() + "' is not active!");
        } else {
            log.info("Stop MessageQueueMDB service '" + this.getClass().getName() + "'");

            try {
                InitialContext ic = new InitialContext();
                ((Scheduler) ic.lookup("global/restamq/SchedulerBean!restamq.Scheduler")).stop();
            } catch (NamingException e) {
                log.info(e.getMessage(), e);
            }
        }
    }

    @Override
    public String getValue() throws IllegalStateException, IllegalArgumentException {
        log.info(String.format("%s is %s at %s", MessageQueueMDB.class.getSimpleName(), (started.get() ? "started" : "not started"), System.getProperty("jboss.node.name")));
        return "";
    }
}
