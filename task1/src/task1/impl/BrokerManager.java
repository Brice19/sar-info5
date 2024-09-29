package task1.impl;

import java.util.HashMap;

import task1.abstact.Broker;

public class BrokerManager {

    private static BrokerManager self;

    static BrokerManager getSelf() {
        if (self == null) {
            self = new BrokerManager();
        }
        return self;
    }

    static {
        self = new BrokerManager();
    }

    static HashMap<String, Broker> brokers;
    
    private BrokerManager() {
        brokers = new HashMap<String, Broker>();
    }

    public static synchronized void addBroker(Broker broker) {
        String name = broker.getName();
        Broker b = brokers.get(name);
        if (b == null) {
            brokers.put(name, broker);
        } else {
            throw new RuntimeException("Broker with name " + name + " already exists");
        }
    }

    public synchronized void removeBroker(Broker broker) {
        brokers.remove(broker.getName());
    }

    public synchronized Broker getBroker(String name) {
        return brokers.get(name);
    }

}

