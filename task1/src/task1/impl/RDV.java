package task1.impl;

import task1.abstact.Broker;
import task1.abstact.Channel;

public class RDV {

    MyChannel accChannel;
    MyChannel conChannel;
    Broker accBroker;
    Broker conBroker;

    private void myWait() {
        try {
            wait();
        } catch (InterruptedException e) {
        }
    }

    synchronized Channel accept(Broker accBroker, int port) {
        this.accBroker = accBroker;
        accChannel = new MyChannel(accBroker, port);
        if (conChannel != null) {
            accChannel.connect(conChannel, accBroker.getName());
            notify();
        } else {
            myWait();
            
        }
        return accChannel;
    }

    synchronized Channel connect(Broker conBroker, int port) {
        this.conBroker = conBroker;
        conChannel = new MyChannel(conBroker, port);
        if (accChannel != null) {
            accChannel.connect(conChannel, conBroker.getName());
            notify();
        } else {
            myWait();
            
        }
        return conChannel;
    }
}
