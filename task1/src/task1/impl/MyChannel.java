package task1.impl;

import task1.abst.Broker;
import task1.abst.Channel;
import utils.CircularBuffer;

/**
 * MyChannel class represents a communication channel between two brokers.
 * It uses a CircularBuffer for managing incoming and outgoing data.
 */
public class MyChannel extends Channel {

    // Port number associated with the channel
    private int port;

    // Circular buffers for managing data flow
    private CircularBuffer in, out;

    // Flag indicating if the channel is disconnected
    private Boolean disconnected = false;

    // Reference to the remote channel
    private MyChannel rch;

    // Indicates if the channel is dangling (i.e., the remote channel has been disconnected)
    private boolean dangling;

    // Name of the remote broker this channel is connected to
    private String rname;

    /**
     * Constructs a MyChannel instance associated with a specific broker and port.
     * Initializes the CircularBuffer with a default size.
     *
     * @param broker The broker associated with this channel
     * @param port   The port number for the channel
     */
    protected MyChannel(Broker broker, int port) {
        super(); // Calls the superclass constructor
        this.port = port;
        // Initializes the circular buffer with a capacity of 64 bytes
        this.in = new CircularBuffer(64);
    }

    /**
     * Establishes a connection between this channel and another channel (rch).
     * Sets up the input and output buffers for bidirectional communication.
     *
     * @param rch  The remote channel to connect to
     * @param name The name of the remote broker
     */
    void connect(MyChannel rch, String name) {
        this.rch = rch;
        rch.rch = this;
        this.out = rch.in; // Output buffer of this channel is the input buffer of the remote channel
        rch.out = this.in; // Output buffer of the remote channel is the input buffer of this channel
        this.rname = name;
    }

    /**
     * Disconnects the channel and notifies all waiting threads.
     * Sets the 'disconnected' flag to true and the 'dangling' flag of the remote channel to true.
     */
    @Override
    public void disconnect() {
        synchronized (this) {
            if (disconnected()) {
                return;
            }
            disconnected = true;
            rch.dangling = true;
        }
        synchronized (out) {
            out.notifyAll(); // Notify all threads waiting on the output buffer
        }
        synchronized (in) {
            in.notifyAll(); // Notify all threads waiting on the input buffer
        }
    }

    /**
     * Checks if the channel is disconnected.
     *
     * @return true if the channel is disconnected, false otherwise
     */
    @Override
    public boolean disconnected() {
        return disconnected;
    }

    /**
     * Reads data from the input buffer into the provided byte array.
     * Waits if the buffer is empty and throws a DisconnectedException if the channel is disconnected.
     *
     * @param bytes The byte array to store the read data
     * @param off   The starting offset in the byte array
     * @param len   The maximum number of bytes to read
     * @return The number of bytes read
     * @throws DisconnectedException if the channel is disconnected
     */
    public int read(byte[] bytes, int off, int len) throws DisconnectedException {
        if (disconnected()) {
            throw new DisconnectedException("Channel is disconnected");
        }
        int nb = 0;
        try {
            // Continues reading until data is available in the buffer
            while (nb == 0) {
                if (in.empty()) {
                    synchronized (in) {
                        while (in.empty()) {
                            if (disconnected() || dangling) {
                                throw new DisconnectedException("Channel is disconnected");
                            }
                            try {
                                in.wait(); // Waits until data is available in the input buffer
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt(); // Preserve interrupt status
                            }
                        }
                    }
                }
                // Reads bytes from the input buffer into the provided byte array
                while (nb < len && !in.empty()) {
                    byte c = in.pull();
                    bytes[off + nb] = c;
                    nb++;
                }
                if (nb != 0) {
                    synchronized (in) {
                        in.notify(); // Notify threads that new data has been read from the input buffer
                    }
                }
            }
        } catch (DisconnectedException e) {
            // If an exception occurs during read, set disconnected flag and notify all
            if (!disconnected()) {
                disconnected = true;
                synchronized (out) {
                    out.notifyAll();
                }
            }
            throw e;
        }
        return nb;
    }

    /**
     * Writes data from the provided byte array to the output buffer.
     * Waits if the buffer is full and throws a DisconnectedException if the channel is disconnected.
     *
     * @param bytes The byte array containing the data to write
     * @param off   The starting offset in the byte array
     * @param len   The number of bytes to write
     * @return The number of bytes written
     * @throws DisconnectedException if the channel is disconnected
     */
    public int write(byte[] bytes, int off, int len) throws DisconnectedException {
        if (disconnected()) {
            throw new DisconnectedException("Channel is disconnected");
        }
        int nb = 0;
        // Continues writing until data can be successfully written to the output buffer
        while (nb == 0) {
            if (out.full()) {
                synchronized (out) {
                    while (out.full()) {
                        if (disconnected()) {
                            throw new DisconnectedException("Channel is disconnected");
                        }
                        if (dangling) {
                            return len; // Returns len if the remote channel is dangling (disconnected)
                        }
                        try {
                            out.wait(); // Waits until space is available in the output buffer
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt(); // Preserve interrupt status
                        }
                    }
                }
            }
            // Writes bytes to the output buffer from the provided byte array
            while (nb < len && !out.full()) {
                byte c = bytes[off + nb];
                out.push(c);
                nb++;
            }
            if (nb != 0) {
                synchronized (out) {
                    out.notify(); // Notify threads that new data has been written to the output buffer
                }
            }
        }
        return nb;
    }
}
