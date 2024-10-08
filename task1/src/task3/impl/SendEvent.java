package task3.impl;

public class SendEvent implements Runnable {

    private final MyMessageQueue queue;
    private final Message message;

    /**
     * Constructs a SendEvent to send a message.
     *
     * @param queue   The MessageQueue through which the message is sent
     * @param message The message to be sent
     */
    public SendEvent(MyMessageQueue queue, Message message) {
        this.queue = queue;
        this.message = message;
    }

    @Override
    public void run() {
        // Add the message to the queue and notify the listener
        queue.send(message);
    }

}
