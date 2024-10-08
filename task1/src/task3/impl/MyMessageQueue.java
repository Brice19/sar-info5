package task3.impl;

import java.net.http.WebSocket.Listener;
import java.nio.ByteBuffer;

import task1.abst.Channel;
import task2.abst.MessageQueue;

public class MyMessageQueue extends MessageQueue {

    // The Channel used for sending and receiving messages
    Listener channelListener;
    private boolean isClosed = false;

    public void setListener(Listener l){
        this.channelListener = l;
    }

	public boolean send(Message msg){
        byte[] sizeBytes = ByteBuffer.allocate(Integer.BYTES).putInt(msg.getLength()).array();
        byte[] buffer = new byte[sizeBytes.length + msg.getLength()];

        System.arraycopy(sizeBytes, 0, buffer, 0, sizeBytes.length);
        System.arraycopy(msg.getBytes(), msg.getOffset(), buffer, sizeBytes.length, msg.getLength());

        // TODO: Implement sending the message through the channel

    }

	public void close(){
        isClosed = true;

    }

	public boolean closed(){
return isClosed;
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

}
