package task3.impl;

import task1.impl.MyBroker;
import task3.abst.QueueBroker;

public class MyQueueBroker extends QueueBroker {

    // Name of the QueueBroker
    private String name;

    // Broker used internally to handle channel-level connections
    private MyBroker broker;

    // HashMap to store the port and the corresponding listener
    private HashMap<Integer, AcceptListener> portListenerMap = new HashMap<>();

    public MyQueueBroker(String name) {

        this.broker = new MyBroker(name);
        this.name = name;
    }

    public boolean bind(int port, AcceptListener listener) {
//TODO
    }

    public boolean unbind(int port) {
//TODO
    }

    public boolean connect(String name, int port, ConnectListener listener) {
//TODO
    }



    

    

}
