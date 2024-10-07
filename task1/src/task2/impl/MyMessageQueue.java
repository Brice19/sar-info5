package task2.impl;

import java.nio.ByteBuffer;
import task1.abst.Channel;
import task2.abst.MessageQueue;

/**
 * MyMessageQueue is a concrete implementation of the abstract MessageQueue class.
 * It handles the sending and receiving of complete messages using a Channel.
 */
public class MyMessageQueue extends MessageQueue {

    // The Channel used for sending and receiving messages
    private Channel channel;

    /**
     * Constructor that initializes the MessageQueue with a Channel.
     *
     * @param channel The Channel to be used for message communication
     */
    public MyMessageQueue(Channel channel) {
        this.channel = channel;
    }

    /**
     * Sends a message as a byte array through the Channel.
     * It first sends the size of the message, followed by the message content.
     *
     * @param bytes  The byte array containing the message data
     * @param offset The starting position in the byte array
     * @param length The number of bytes to send from the byte array
     */
    @Override
    public void send(byte[] bytes, int offset, int length) {
        // Allocate a buffer to hold the size of the message (4 bytes) and the message itself
        byte[] sizeBytes = ByteBuffer.allocate(Integer.BYTES).putInt(length).array();
        byte[] buffer = new byte[sizeBytes.length + length];
        
        // Copy the size and message into the buffer
        System.arraycopy(sizeBytes, 0, buffer, 0, sizeBytes.length);
        System.arraycopy(bytes, offset, buffer, sizeBytes.length, length);

        // Send the buffer through the channel
        int sentBytes = 0;
        while (sentBytes < buffer.length) {
            sentBytes += channel.write(buffer, sentBytes, buffer.length - sentBytes);
        }
    }

    /**
     * Receives a message from the Channel. It first reads the size of the message,
     * then reads the message itself based on the size.
     *
     * @return The byte array containing the received message, or null if disconnected
     */
    @Override
    public byte[] receive() {
        // Read the first 4 bytes to get the message size
        int messageSize = readMessageSize(channel);
        
        if (messageSize <= 0) {
            return null; // Return null if the channel is disconnected or no message is available
        }

        // Allocate a buffer for the message based on the size
        byte[] buffer = new byte[messageSize];
        int bytesRead = 0;

        // Read the message content from the channel
        while (bytesRead < messageSize) {
            int response = channel.read(buffer, bytesRead, messageSize - bytesRead);
            bytesRead += response;
        }
        return buffer;
    }

    /**
     * Reads the size of the incoming message from the Channel.
     * It reads 4 bytes (size of an integer) and converts it to an integer.
     *
     * @param channel The Channel to read from
     * @return The size of the message as an integer
     */
    private static int readMessageSize(Channel channel) {
        byte[] sizeBytes = new byte[4];
        int bytesRead = 0;
        int response;

        // Read 4 bytes to get the size of the message
        while (bytesRead < 4) {
            response = channel.read(sizeBytes, bytesRead, 4 - bytesRead);
            if (response == -1) {
                return -1;
            }
            bytesRead += response;
        }
        return ByteBuffer.wrap(sizeBytes).getInt();
    }

    /**
     * Closes the MessageQueue by disconnecting the underlying Channel.
     */
    @Override
    public void close() {
        channel.disconnect();
    }

    /**
     * Checks if the MessageQueue is closed.
     *
     * @return true if the underlying Channel is disconnected, false otherwise
     */
    @Override
    public boolean closed() {
        return channel.disconnected();
    }
}
