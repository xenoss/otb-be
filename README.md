System requirements
-------------------
All you need to build this project is Java 8 (Java SDK 1.8) or better, Maven 3.1 or better.

Install Aerospike on local machine
-------------------------
Follow the setup instructions:

    https://www.aerospike.com/docs/operations/install/index.html

Configure Aerospike
-------------------------
Create an in memory namespace test_namespace with a 1GB capacity.

    namespace test_namespace {
        memory-size 1G           # 4GB of memory to be used for index and data
        # replication-factor 2     # For multiple nodes, keep 2 copies of the data
        # high-water-memory-pct 60 # Evict non-zero TTL data if capacity exceeds
                               # 60% of 4GB
        # stop-writes-pct 90       # Stop writes if capacity exceeds 90% of 4GB
        # default-ttl 0            # Writes from client that do not provide a TTL
                               # will default to 0 or never expire
        # storage-engine memory    # Store data in memory only
    }

Install WildFly
-------------------------
WildFly 10 distributions can be obtained from:
    http://www.wildfly.org/downloads/

Start JBoss WildFly with a HA profile
-------------------------
If you run a non HA profile the singleton service will not start correctly. To run the example one instance must be started, to see the singleton behaviour at minimum two instances
should be started.
    
    Start server one : standalone.sh --server-config=standalone-full-ha.xml -Djboss.node.name=nodeOne
    Start server two : standalone.sh --server-config=standalone-full-ha.xml -Djboss.node.name=nodeTwo -Djboss.socket.binding.port-offset=100
    
Build and Deploy the application
-------------------------
1. Make sure you have started the JBoss WildFly Server as described above.
2. Open a command line and navigate to the root directory of this project.
3. Type this command to build and deploy the archive:

    mvn clean install

4. Type this command to deploy the archive to the second server (or more) and replace the port, depending on your settings:

    mvn install -Dwildfly.port=10090

Check whether the application is deployed on each instance.
All instances will have a message:
INFO  [org.jboss.as.clustering.singleton] (SingletonService lifecycle - 1) JBAS010342: nodeOne/cluster elected as the singleton provider of the jboss.quickstart.ejb.ha.singleton service
Only nodeOne (or even one instance) will have a message:
INFO  [org.jboss.as.clustering.singleton] (SingletonService lifecycle - 1) JBAS010340: This node will now operate as the singleton provider of the jboss.quickstart.ejb.ha.singleton service

The HA singleton service on the started node will takes a message every 20sec.
