package task1.impl;

import java.util.HashMap;

import task1.abst.Broker;

/**
 * The BrokerManager class is a singleton that manages a collection of Brokers.
 * This class allows adding, removing, and retrieving Brokers by their name.
 */
public class BrokerManager {

    // Singleton instance of the BrokerManager
    private static BrokerManager instance;

    /**
     * Returns the singleton instance of BrokerManager. 
     * This method initializes the instance if it hasn't been created yet.
     *
     * @return BrokerManager singleton instance
     */
    public static BrokerManager getSelf() {
        if (instance == null) {
            instance = new BrokerManager();
        }
        return instance;
    }

    // Static initializer to ensure instance is created when the class is loaded
    static {
        instance = new BrokerManager();
    }

    // HashMap to store Broker instances identified by their unique names
    private HashMap<String, Broker> brokers;

    /**
     * Private constructor to prevent instantiation outside of the class.
     * Initializes the brokers HashMap.
     */
    private BrokerManager() {
        brokers = new HashMap<>();
    }

    /**
     * Adds a Broker to the collection.
     * The method is synchronized to ensure thread-safety.
     *
     * @param broker The Broker to be added
     * @throws RuntimeException if a Broker with the same name already exists
     */
    public synchronized void addBroker(Broker broker) {
        String name = broker.getName();
        Broker existingBroker = brokers.get(name);
        if (existingBroker == null) {
            brokers.put(name, broker);
        } else {
            // Throws an exception if a Broker with the same name is already present
            throw new RuntimeException("Broker with name " + name + " already exists");
        }
    }

    /**
     * Removes a Broker from the collection based on its name.
     * The method is synchronized to ensure thread-safety.
     *
     * @param broker The Broker to be removed
     */
    public synchronized void removeBroker(Broker broker) {
        brokers.remove(broker.getName());
    }

    /**
     * Retrieves a Broker by its name.
     * The method is synchronized to ensure thread-safety.
     *
     * @param name The name of the Broker to retrieve
     * @return The Broker associated with the given name, or null if not found
     */
    public synchronized Broker getBroker(String name) {
        return brokers.get(name);
    }
}
