package restamq;

import lombok.val;
import org.jboss.logging.Logger;
import org.jboss.msc.service.*;
import org.wildfly.clustering.singleton.SingletonServiceBuilderFactory;
import org.wildfly.clustering.singleton.SingletonServiceName;
import org.wildfly.clustering.singleton.election.SimpleSingletonElectionPolicy;

public class MessageQueueMDBActivator implements ServiceActivator {
    private static final Logger log = Logger.getLogger(MessageQueueMDBActivator.class.toString());

    @Override
    public void activate(ServiceActivatorContext context) throws ServiceRegistryException {
        log.info("MessageQueueMDB will be installed!");

        val service = new MessageQueueMDB();
        ServiceName factoryServiceName = SingletonServiceName.BUILDER.getServiceName("server", "default");
        ServiceController<?> factoryService = context.getServiceRegistry().getRequiredService(factoryServiceName);
        SingletonServiceBuilderFactory factory = (SingletonServiceBuilderFactory) factoryService.getValue();
        factory
                .createSingletonServiceBuilder(MessageQueueMDB.SINGLETON_SERVICE_NAME, service)
                .electionPolicy(new SimpleSingletonElectionPolicy())
                .build(new DelegatingServiceContainer(context.getServiceTarget(), context.getServiceRegistry()))
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();
    }
}
