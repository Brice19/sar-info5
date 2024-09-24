package task1.impl;
import task1.abstact.*;
import utils.CircularBuffer;

public class MyChannel extends Channel {

    private final CircularBuffer buffer;
    private volatile boolean isDisconnected;

    public MyChannel(int capacity) {
        this.buffer = new CircularBuffer(capacity);
        this.isDisconnected = false;
    }

    @Override
    public synchronized int write(byte[] bytes, int offset, int length) {
        if (isDisconnected) {
        	throw new IllegalStateException("Channel is disconnected");
        }

        int bytesWritten = 0;
        for (int i = 0; i < length; i++) {
            try {
                buffer.push(bytes[offset + i]);
                bytesWritten++;
            } catch (IllegalStateException e) {
                // Buffer plein, on attend qu'il y ait de la place
                while (buffer.full()) {
                    try {
                        wait(); // Attendre que le buffer se vide un peu
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt(); // Bonne pratique pour gérer les interruptions
                    }
                }
            }
        }
        notifyAll(); // Notifier les threads en attente de lecture
        return bytesWritten;
    }

    @Override
    public synchronized int read(byte[] bytes, int offset, int length) {
        if (isDisconnected && buffer.empty()) {
        	 throw new IllegalStateException("Channel is disconnected and no more data to read");
        }

        int bytesRead = 0;
        for (int i = 0; i < length; i++) {
            try {
                bytes[offset + i] = buffer.pull();
                bytesRead++;
            } catch (IllegalStateException e) {
                // Buffer vide, on attend que des données arrivent
                while (buffer.empty()) {
                    if (isDisconnected) {
                    	 throw new IllegalStateException("Channel diconected while reading");
                    }
                    try {
                        wait(); // Attendre que des données soient disponibles
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        notifyAll(); // Notifier les threads en attente d'écriture
        return bytesRead;
    }

    @Override
    public synchronized void disconnect() {
        isDisconnected = true;
        notifyAll(); // Réveiller tous les threads bloqués sur lecture/écriture
    }

    @Override
    public boolean disconnected() {
        return isDisconnected;
    }
}
