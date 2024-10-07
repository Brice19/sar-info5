package task2.test;

import task2.abst.BaseBrokerAbstract;
import task2.abst.MessageQueue;
import task2.abst.QueueBroker;
import task2.impl.MyQueueBroker;
import task2.impl.MyTask;

public class example {
	    
    private void validate(byte[] echoBuffer, String message) {
        if(echoBuffer == null) System.err.println("echoBuffer is null, but it shouldn't be.");
        if(!new String(echoBuffer, 0, echoBuffer.length).equals(message)) System.err.println("Message content mismatch.");
    }

    protected static Boolean VERBOSE = true;
    public static void main(String[] args) {
        // Create a new test object
        example test = new example();
        // Run the test
        test.test1();
    }

    protected class EchoServer implements Runnable {
        protected BaseBrokerAbstract broker;
        protected boolean isAccept;
        protected String brokerName;
        protected int port;

        public EchoServer(QueueBroker broker2, boolean isAccept, String brokerName, int port) {
            this.broker = broker2;
            this.isAccept = isAccept;
            this.brokerName = brokerName;
            this.port = port;
        }
        @Override
        public void run() {
            String msg = "Message super long qui raconte l'histoire d'un petit poussin qui c'etait perdus, le grand ours vint alors lui tenir la main et le racompagna.";
                    	
            try {
            	MessageQueue serverChannel;
                int nbMessages = 0;
                if (isAccept) {
                    serverChannel = (MessageQueue) broker.accept(this.port);
                
                    while (nbMessages < 10) {
                    	
                    	byte[] recept = serverChannel.receive();
                    	
                        validate(recept, msg);

                    	if (VERBOSE) {
                            System.out.println("Read message: " + new String(recept, 0, recept.length));
                        }
                    	
                    	serverChannel.send(recept, 0, recept.length);
                    	
                        nbMessages++;
                    }

                } else {
                    serverChannel = (MessageQueue) broker.connect(this.brokerName, this.port);

                    while (nbMessages < 10) {
                    	
                    	serverChannel.send(msg.getBytes(), 0, msg.getBytes().length);
                    	
                    	byte[] recept = serverChannel.receive();
                        
                        validate(recept, msg);

                    	if (VERBOSE) {
                            System.out.println("Read message: " + new String(recept, 0, recept.length));
                        }
                    	
                        nbMessages++;
                    }
                }
                
                serverChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }

    public void test1() {
        QueueBroker broker = new MyQueueBroker("BrokerT1");

        EchoServer ecServ1 = new EchoServer(broker, true, "BrokerT1", 8080);
        EchoServer ecServ2 = new EchoServer(broker, false, "BrokerT1", 8080);
        
        MyTask serverTask = new MyTask(broker, ecServ1);
        MyTask clientTask = new MyTask(broker, ecServ2);
        
        try {
        	serverTask.join();
        	clientTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}