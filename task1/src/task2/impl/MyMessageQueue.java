package task2.impl;

import java.util.LinkedList;
import java.util.Queue;

import task2.abst.MessageQueue;

/**
 * MyMessageQueue is a concrete implementation of the abstract MessageQueue class.
 * It manages the sending and receiving of complete messages.
 */
public class MyMessageQueue extends MessageQueue {

    // Internal queue to hold messages as byte arrays
    private Queue<byte[]> queue;
    private boolean closed;

    /**
     * Constructor to initialize the internal queue.
     */
    public MyMessageQueue() {
        this.queue = new LinkedList<>();
        this.closed = false;
    }

    @Override
    public synchronized void send(byte[] bytes, int offset, int length) {
        if (closed) {
            throw new RuntimeException("Cannot send message: Queue is closed.");
        }
        byte[] message = new byte[length];
        System.arraycopy(bytes, offset, message, 0, length);
        queue.offer(message);
        notifyAll(); // Notify any waiting threads that a new message has been added
    }

    @Override
    public synchronized byte[] receive() {
        while (queue.isEmpty() && !closed) {
            try {
                wait(); // Wait until a message is available or the queue is closed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (queue.isEmpty() && closed) {
            return null; // Return null if the queue is closed and empty
        }
        return queue.poll(); // Retrieve and remove the first message from the queue
    }

    @Override
    public synchronized void close() {
        closed = true;
        notifyAll(); // Notify all waiting threads that the queue is closed
    }

    @Override
    public boolean closed() {
        return closed;
    }
}
