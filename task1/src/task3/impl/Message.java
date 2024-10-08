package task3.impl;

public class Message {

	byte[] bytes;
	int offset;
	int length;

	public Message(byte[] bytes, int offset, int length) {
		this.bytes = bytes;
		this.offset = offset;
		this.length = length;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}



}
