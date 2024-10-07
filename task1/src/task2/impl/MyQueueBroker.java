package task2.impl;

import task1.impl.MyBroker;
import task2.abst.MessageQueue;
import task2.abst.QueueBroker;

/**
 * MyQueueBroker is a concrete implementation of the abstract QueueBroker class.
 * It manages MessageQueues by utilizing an internal Broker for establishing 
 * channel-based connections.
 */
public class MyQueueBroker extends QueueBroker {

    // Name of the QueueBroker
    private String name;

    // Broker used internally to handle channel-level connections
    private MyBroker broker;

    /**
     * Constructor to create a MyQueueBroker instance with a specified name.
     * Initializes the internal Broker instance and registers it with BrokerManager.
     *
     * @param name The name of the QueueBroker
     */
    public MyQueueBroker(MyBroker broker) {
        // Create a new Broker with the same name
        this.broker = broker;
        this.name = this.broker.getName();

    }
    
    public MyQueueBroker(String name) {
		this.broker = new MyBroker(name);
		this.name = name;
	}
    

    /**
     * Returns the name of the QueueBroker.
     *
     * @return The name of the QueueBroker
     */
    @Override
    public String name() {
        return this.name;
    }

    /**
     * Accepts a connection on a specified port and returns a new MyMessageQueue.
     * Utilizes the internal Broker to establish the connection and wrap the channel.
     *
     * @param port The port number to accept connections on
     * @return MyMessageQueue established for the accepted connection
     */
    @Override
    public MessageQueue accept(int port) {
        // Use the internal Broker to accept a connection and obtain a ChannelAbstract
        var channel = broker.accept(port);
        // Wrap the ChannelAbstract in a MyMessageQueue and return it
        return new MyMessageQueue(channel);
    }

    /**
     * Connects to another QueueBroker by its name and port, and returns a new MyMessageQueue.
     * Utilizes the internal Broker to establish the connection and wrap the channel.
     *
     * @param name The name of the target QueueBroker to connect to
     * @param port The port number for the connection
     * @return MyMessageQueue established between the current and the target QueueBroker
     */
    @Override
    public MessageQueue connect(String name, int port) {
        // Use the internal Broker to connect to the target QueueBroker's port
        var channel = broker.connect(name, port);
        // Wrap the ChannelAbstract in a MyMessageQueue and return it
        return new MyMessageQueue(channel);
    }
}
