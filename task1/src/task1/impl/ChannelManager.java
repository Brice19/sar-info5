package task1.impl;

import utils.CircularBuffer;

public class ChannelManager {

	public MyChannel connectingBrokerChannel;
	public MyChannel acceptingBrokerChannel;

	public ChannelManager() {

		CircularBuffer circularBuffer1 = new CircularBuffer(1024);
		CircularBuffer circularBuffer2 = new CircularBuffer(1024);

		MyChannel connectingChannel = new MyChannel(this, circularBuffer1, circularBuffer2);
		MyChannel acceptingChannel = new MyChannel(this, circularBuffer2, circularBuffer1);

		this.connectingBrokerChannel = connectingChannel;
		this.acceptingBrokerChannel = acceptingChannel;
	}

	public synchronized void disconnect(MyChannel channel) {
		// Guarented the order of disconnection
		if (channel == connectingBrokerChannel) {
			connectingBrokerChannel.disconnect();
			acceptingBrokerChannel.disconnect();
		} else if (channel == acceptingBrokerChannel) {
			acceptingBrokerChannel.disconnect();
			connectingBrokerChannel.disconnect();
		}
	}

}
