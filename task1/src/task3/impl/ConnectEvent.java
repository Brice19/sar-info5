package task3.impl;

import task3.abst.QueueBroker;
import task3.abst.QueueBroker.ConnectListener;

public class ConnectEvent implements Runnable {

    private final QueueBroker broker;
    private final String targetName;
    private final int port;
    private final ConnectListener listener;

    /**
     * Constructs a ConnectEvent to handle connection requests.
     *
     * @param broker     The QueueBroker initiating the connection
     * @param targetName The name of the target QueueBroker to connect to
     * @param port       The port of the target QueueBroker
     * @param listener   The listener to handle connection success or failure
     */
    public ConnectEvent(QueueBroker broker, String targetName, int port, ConnectListener listener) {
        this.broker = broker;
        this.targetName = targetName;
        this.port = port;
        this.listener = listener;
    }

    @Override
    public void run() {
        // Initiate the connection to the target QueueBroker
        boolean connected = broker.connect(targetName, port, listener);

        // Notify the listener if the connection was established or refused
        if (connected) {
            listener.connected(null);
        } else {
            listener.refused();
        }
    }
}
