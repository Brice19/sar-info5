package task1.impl;
import java.io.IOException;

import task1.abst.Broker;
import task1.abst.Channel;
import utils.CircularBuffer;

public class MyChannel extends Channel {

    int port;
    CircularBuffer in, out;
    Boolean disconnected = false;
    MyChannel rch;
    boolean dangling;
    String rname;

    protected MyChannel(Broker broker, int port) {
        super();	//super(broker)
        this.port = port;
        this.in = new CircularBuffer(64);
    }

    void connect(MyChannel rch, String name){
        this.rch = rch;
        rch.rch = this;
        this.out = rch.in;
        rch.out = this.in;
        this.rname = name;
    }

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
            out.notifyAll();
        }
        synchronized (in) {
            in.notifyAll();
        }
    }

    @Override
    public boolean disconnected() {
        return disconnected;
    }

    public int read(byte[] bytes, int off, int len) throws DisconnectedException {
        if (disconnected()) {
            throw new DisconnectedException("Channel is disconnected");
        }
        int nb = 0;
        try {
            while(nb == 0) {
                if (in.empty()) {
                    synchronized (in) {
                        while (in.empty()) {
                            if (disconnected() || dangling) {
                                throw new DisconnectedException("Channel is disconnected");
                            }
                            try {
                                in.wait();
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
                while (nb < len && !in.empty()) {
                    byte c = in.pull();
                    bytes[off + nb] = c;
                    nb++;
                }
                if (nb != 0) {
                    synchronized (in) {
                        in.notify();
                    }
                }
            } 
         } catch (DisconnectedException e) {
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

        public int write(byte[] bytes, int off, int len) throws DisconnectedException {
            if (disconnected()) {
                throw new DisconnectedException("Channel is disconnected");
            }
            int nb = 0;
            while (nb == 0) {
                if (out.full()) {
                    synchronized (out) {
                        while (out.full()) {
                            if (disconnected()){
                                throw new DisconnectedException("Channel is disconnected");
                            }
                            if (dangling) {
                                return len;
                            }
                            try {
                                out.wait();
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
                while (nb < len && !out.full()) {
                    byte c = bytes[off + nb];
                    out.push(c);
                    nb++;
                }
                if (nb != 0) {
                    synchronized (out) {
                        out.notify();
                    }
                }
            
            }
            return nb;
        }
}
