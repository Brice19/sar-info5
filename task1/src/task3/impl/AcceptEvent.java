package task3.impl;

import task3.abst.QueueBroker;
import task3.abst.QueueBroker.AcceptListener;

public class AcceptEvent implements Runnable {

    private final QueueBroker broker;
    private final int port;
    private final AcceptListener listener;

    /**
     * Constructs an AcceptEvent to handle accepting a connection.
     *
     * @param broker   The QueueBroker accepting the connection
     * @param port     The port number to accept connections on
     * @param listener The listener to handle accepted connections
     */
    public AcceptEvent(QueueBroker broker, int port, AcceptListener listener) {
        this.broker = broker;
        this.port = port;
        this.listener = listener;
    }

    @Override
    public void run() {
        // Accept the connection on the specified port and notify the listener
        broker.bind(port, listener);
       
    }
}