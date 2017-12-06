Start JBoss WildFly with a HA profile
-------------------------
    
    Start server one : standalone.sh --server-config=standalone-full-ha.xml -Djboss.node.name=nodeOne
    Start server two : standalone.sh --server-config=standalone-full-ha.xml -Djboss.node.name=nodeTwo -Djboss.socket.binding.port-offset=100
    
Build and Deploy the Quickstart
-------------------------

    mvn clean install
    mvn install -Dwildfly.port=10090
