package task1;

abstract class Channel {
	abstract int read(byte[] bytes, int offset, int length);

	abstract int write(byte[] bytes, int offset, int length);

	abstract void disconnect();

	abstract boolean disconnected();
}
