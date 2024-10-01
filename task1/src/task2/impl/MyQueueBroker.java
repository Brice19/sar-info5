package task2.impl;

import java.util.HashMap;

import task1.abst.Broker;
import task1.impl.BrokerManager;
import task2.abst.MessageQueue;
import task2.abst.QueueBroker;

/**
 * MyQueueBroker is an implementation of the QueueBroker class,
 * managing the creation and synchronization of MessageQueues.
 */
public class MyQueueBroker extends QueueBroker {

    // HashMap to store the active message queues for different ports
    private HashMap<Integer, MessageQueue> messageQueues;

    /**
     * Constructor to initialize MyQueueBroker with a specified Broker.
     *
     * @param broker The broker associated with this QueueBroker
     */
    public MyQueueBroker(Broker broker) {
        super(broker);
        this.messageQueues = new HashMap<>();
    }

    /**
     * Returns the name of the QueueBroker, which is the same as the associated broker's name.
     *
     * @return The name of the QueueBroker
     */
    @Override
    public String name() {
        return broker.getName(); // Uses the broker's name as the QueueBroker name
    }

    /**
     * Accepts a connection on a specific port and creates a MessageQueue for communication.
     *
     * @param port The port to accept the connection on
     * @return The MessageQueue associated with the accepted connection
     */
    @Override
    public synchronized MessageQueue accept(int port) {
        if (messageQueues.containsKey(port)) {
            throw new RuntimeException("Port " + port + " already in use.");
        }
        MessageQueue queue = new MyMessageQueue(); // Creates a new MessageQueue instance
        messageQueues.put(port, queue);
        notifyAll(); // Notify any waiting threads that a connection has been accepted
        return queue;
    }

    /**
     * Connects to another QueueBroker by name and port, creating a MessageQueue for communication.
     *
     * @param name The name of the QueueBroker to connect to
     * @param port The port to connect on
     * @return The MessageQueue associated with the established connection
     */
    @Override
    public synchronized MessageQueue connect(String name, int port) {
        MyQueueBroker targetBroker = (MyQueueBroker) BrokerManager.getSelf().getBroker(name);
        if (targetBroker == null) {
            throw new RuntimeException("QueueBroker with name " + name + " does not exist.");
        }
        // Wait for the target broker to accept the connection on the given port
        synchronized (targetBroker) {
            while (!targetBroker.messageQueues.containsKey(port)) {
                try {
                    targetBroker.wait(); // Wait until the target broker accepts the connection
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        // Retrieve the existing MessageQueue from the target broker
        MessageQueue queue = targetBroker.messageQueues.get(port);
        return queue;
    }
}
