package task1.impl;

import java.util.HashMap;

import task1.abst.Broker;
import task1.abst.Channel;

/**
 * MyBroker is a concrete implementation of the abstract Broker class.
 * It manages connections and channels using the RDV class and interacts
 * with the BrokerManager for broker registration and retrieval.
 */
public class MyBroker extends Broker {

    // Instance of BrokerManager to manage brokers
    private BrokerManager manager;

    // Map of RDVs to manage port connections (Integer: Port, RDV: Rendezvous)
    private HashMap<Integer, RDV> accepts;

    /**
     * Constructor to create a new MyBroker instance with a specified name.
     * It registers itself with the BrokerManager upon creation.
     *
     * @param name Name of the broker
     */
    public MyBroker(String name) {
        super(name);
        accepts = new HashMap<>();
        manager = BrokerManager.getSelf(); // Changed to getInstance for consistency
        manager.addBroker(this);    
    }

    /**
     * Connects to another broker by name and port.
     * It retrieves the target broker and invokes the toConnect method to establish a channel.
     *
     * @param name The name of the target broker to connect to
     * @param port The port number for the connection
     * @return Channel established between the current and the target broker
     * @throws RuntimeException if the target broker does not exist
     */
    @Override
    public Channel connect(String name, int port) {
        // Retrieves the target broker by name
        MyBroker targetBroker = (MyBroker) manager.getBroker(name);
        if (targetBroker == null) {
            throw new RuntimeException("Broker with name " + name + " does not exist");
        }
        // Connects to the target broker's port
        return targetBroker.toConnect(this, port);
    }

    /**
     * Accepts an incoming connection on a specified port.
     * If the port is already in use, it throws a RuntimeException.
     *
     * @param port The port number to accept connections on
     * @return Channel established for the accepted connection
     * @throws RuntimeException if the port is already in use
     */
    @Override
    public Channel accept(int port) {
        RDV rdv;
        synchronized (accepts) {
            rdv = accepts.get(port);
            if (rdv != null) {
                throw new RuntimeException("Port " + port + " already in use");
            }
            // Creates a new RDV (Rendezvous) and maps it to the specified port
            rdv = new RDV();
            accepts.put(port, rdv);
            // Notifies any waiting threads that a new RDV is available
            accepts.notifyAll();
        }
        // Accepts the incoming connection through the RDV instance
        return rdv.accept(this, port);
    }

    /**
     * Establishes a connection to another broker's RDV on the specified port.
     * It waits until a connection on the given port is available, if necessary.
     *
     * @param b    The broker that is initiating the connection
     * @param port The port number for the connection
     * @return Channel established for the connection
     */
    private Channel toConnect(MyBroker b, int port) {
        RDV rdv;
        synchronized (accepts) {
            rdv = accepts.get(port);
            while (rdv == null) {
                try {
                    // Waits until an RDV is available on the given port
                    accepts.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Set the interrupted flag
                }
                rdv = accepts.get(port);
            }
            // Once the connection is established, the RDV is removed from the map
            accepts.remove(port);
        }
        return rdv.connect(b, port);
    }

    /**
     * Returns the name of the broker.
     *
     * @return The name of the broker
     */
    @Override
    public String getName() {
        return this.name;
    }
}
