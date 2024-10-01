package task1.impl;

/**
 * DisconnectedException is a custom runtime exception used to indicate
 * that a communication channel has been disconnected unexpectedly.
 * It is thrown when a channel operation (e.g., read or write) is attempted
 * on a disconnected channel, ensuring that the state of the channel
 * is respected during communication.
 */
public class DisconnectedException extends RuntimeException {
    
    /**
     * Constructs a new DisconnectedException with the specified detail message.
     * This message provides additional context about the disconnection.
     *
     * @param message A detail message providing context about the exception
     */
    public DisconnectedException(String message) {
        super(message);
    }
}
