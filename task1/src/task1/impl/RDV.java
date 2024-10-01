package task1.impl;

import task1.abst.Broker;
import task1.abst.Channel;

/**
 * The RDV (Rendezvous) class manages the synchronization between two brokers
 * attempting to establish a channel. It holds references to the accepting
 * and connecting channels and brokers, facilitating their connection.
 */
public class RDV {

    // Channels for the accepting and connecting brokers
    private MyChannel accChannel;
    private MyChannel conChannel;

    // Brokers for the accepting and connecting channels
    private Broker accBroker;
    private Broker conBroker;

    /**
     * Utility method to handle waiting inside a synchronized block.
     * Catches InterruptedException silently but should ideally handle it more robustly.
     */
    private void myWait() {
        try {
            wait(); // Waits until another thread invokes notify or notifyAll
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }

    /**
     * Accepts a connection from a connecting broker. Creates a new MyChannel
     * for the accepting broker and establishes a link with the connecting channel
     * if it already exists.
     *
     * @param accBroker The broker that is accepting the connection
     * @param port      The port number for the connection
     * @return The established channel for the accepting broker
     */
    public synchronized Channel accept(Broker accBroker, int port) {
        this.accBroker = accBroker;
        // Creates a new channel for the accepting broker on the specified port
        accChannel = new MyChannel(accBroker, port);

        // If the connecting channel already exists, establish the connection
        if (conChannel != null) {
            accChannel.connect(conChannel, accBroker.getName());
            notify(); // Notify the connecting broker that the channel is ready
        } else {
            // Waits until the connecting channel is established
            myWait();
        }
        return accChannel;
    }

    /**
     * Connects to an accepting broker. Creates a new MyChannel for the connecting broker
     * and establishes a link with the accepting channel if it already exists.
     *
     * @param conBroker The broker that is initiating the connection
     * @param port      The port number for the connection
     * @return The established channel for the connecting broker
     */
    public synchronized Channel connect(Broker conBroker, int port) {
        this.conBroker = conBroker;
        // Creates a new channel for the connecting broker on the specified port
        conChannel = new MyChannel(conBroker, port);

        // If the accepting channel already exists, establish the connection
        if (accChannel != null) {
            accChannel.connect(conChannel, conBroker.getName());
            notify(); // Notify the accepting broker that the channel is ready
        } else {
            // Waits until the accepting channel is established
            myWait();
        }
        return conChannel;
    }
}
