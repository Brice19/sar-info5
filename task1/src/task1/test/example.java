package task1.test;

import java.util.ArrayList;

import task1.impl.MyBroker;
import task1.impl.MyChannel;
import task1.impl.MyTask;

/**
 * 
 * Classe faite et partagé par Nicolas Picaut
 * Ajuster à mon code
 * 
 * Cette classe met en place un test simple de communication entre deux tâches
 * via un MyMyBroker et des canaux.
 * 
 * - Un MyMyBrokers est créé : `MyBroker1`.
 * - MyMyTask 1 (associée à `MyBroker1`) attend une connexion sur le port 8080 et lit
 * les données reçues via un canal.
 * - MyMyTask 2 (associée à `MyBroker1`) se connecte à `MyBroker1` via le port 8080,
 * envoie un message, puis se déconnecte.
 * - Les deux tâches sont exécutées simultanément et une fois la communication
 * terminée, un message indique la fin.
 * 
 * Ce test illustre l'utilisation de l'API pour établir une communication
 * bidirectionnelle entre tâches.
 */

public class example {

    public final static String LOREM_IPSUM = "\n" + //
            "\n" + //
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed id purus eu est viverra euismod ac sit amet est. Duis tincidunt aliquam condimentum. Donec a aliquam arcu, in gravida ligula. Quisque auctor nisi quam, vitae molestie risus consectetur in. Donec lacinia, velit cursus vulputate laoreet, ante magna consequat dolor, in ornare lectus lacus molestie est. Fusce in sodales nisl. Nullam a augue aliquet, accumsan nibh vitae, viverra nulla. Curabitur molestie libero id iaculis aliquet. Nulla facilisi. Integer ut pharetra est. Vestibulum molestie suscipit elementum. Sed interdum dui eros, in euismod erat suscipit eu.\n"
            + //
            "\n" + //
            "In sit amet accumsan justo. Quisque in nunc diam. Fusce non semper massa, non vestibulum mauris. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Vivamus rhoncus viverra tellus. Aliquam tincidunt augue tortor, id lacinia velit luctus in. Donec pretium ligula at posuere interdum. Nullam eros nisl, lacinia tincidunt mauris ac, sollicitudin aliquam ex. Morbi tempor tempor neque sed finibus. Vestibulum eget tortor sollicitudin, vestibulum ex elementum, tristique nunc. Ut congue lacinia lacus, vitae pretium mauris ultricies sit amet. Praesent faucibus venenatis tortor ac interdum. Quisque vel tincidunt neque. Ut eget efficitur massa.\n"
            + //
            "\n" + //
            "Nunc eget libero nec orci mollis convallis maximus et lectus. Donec ut malesuada mauris, at sollicitudin velit. Etiam et ex commodo, interdum enim et, faucibus arcu. Cras mollis mi massa, vitae efficitur mi ultrices vel. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Morbi aliquam lectus vel sem pharetra facilisis. Nullam eget nisl at ex tempor suscipit. Vestibulum viverra orci id sapien luctus, nec faucibus lectus sagittis. Aenean maximus enim at dolor mollis, non tincidunt purus lacinia. Aliquam aliquam nisi felis. Curabitur elit arcu, fringilla non augue vitae, tempus ornare metus. Integer condimentum massa nec odio gravida scelerisque. Integer scelerisque vitae magna nec imperdiet.\n"
            + //
            "\n" + //
            "Suspendisse libero nisi, efficitur vel mauris vel, mollis fermentum diam. Nam accumsan lectus vitae tincidunt finibus. In tempus interdum arcu. Fusce dignissim venenatis ante id scelerisque. Integer finibus tristique lectus in feugiat. In sed sollicitudin metus, in tempor ligula. In commodo, nisi ut pellentesque convallis, arcu ligula ultricies metus, in faucibus odio quam non nisl. In nunc enim, scelerisque sollicitudin mattis id, ullamcorper nec dui. Mauris gravida mollis neque vitae facilisis. Donec nunc mi, condimentum nec metus in, congue hendrerit magna. Cras nunc eros, porttitor ut turpis in, hendrerit congue diam. Etiam id bibendum sem. Donec tempus erat at arcu maximus, id consequat nibh mattis. Cras diam orci, interdum at accumsan ut, fringilla vitae justo. Sed at efficitur eros, in volutpat odio.\n"
            + //
            "\n" + //
            "Pellentesque ornare est at quam rutrum, efficitur vehicula nibh convallis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam eleifend est viverra odio convallis eleifend. Vivamus nec ipsum vitae nisl efficitur fermentum sit amet nec nisl. Sed eget eros blandit, semper turpis sit amet, faucibus felis. Vestibulum molestie, lacus in blandit lacinia, nulla odio viverra mi, aliquam aliquam est turpis ullamcorper magna. Donec erat diam, vehicula a magna et, finibus commodo velit. Praesent tempus aliquet sem. Curabitur a scelerisque odio. ";

    public static byte[] getMessageSize(int size) {
        byte[] sizeBytes = new byte[4];
        sizeBytes[0] = (byte) (size >> 24);
        sizeBytes[1] = (byte) (size >> 16);
        sizeBytes[2] = (byte) (size >> 8);
        sizeBytes[3] = (byte) size;
        return sizeBytes;
    }

    public static int getSizeFromMessage(byte[] sizeBytes) {
        return (sizeBytes[0] << 24) | (sizeBytes[1] << 16) | (sizeBytes[2] << 8) | sizeBytes[3];
    }

    public static int readMessageSize(MyChannel MyChannel) {
        byte[] sizeBytes = new byte[4];
        // We need to use a While loop to make sure we read all 4 bytes
        int bytesRead = 0;
        int response = 0;
        while (bytesRead < 4) {
            response = MyChannel.read(sizeBytes, bytesRead, 4 - bytesRead);
            if (response == -1) {
                return -1;
            }
            bytesRead += response;
        }
        return getSizeFromMessage(sizeBytes);
    }

    public static byte[] readSizeAndMessage(MyChannel MyChannel) {
        int messageSize = readMessageSize(MyChannel);
        if (messageSize <= 0) {
            return null;
        }

        byte[] buffer = new byte[messageSize];
        int bytesRead = 0;

        while (bytesRead < messageSize) {
            int response = MyChannel.read(buffer, bytesRead, messageSize - bytesRead);

            if (response == -1) {
                return null;
            }

            bytesRead += response;
        }

        if (VERBOSE) {
            System.out.println("Received message: " + new String(buffer, 0, buffer.length));
        }

        return buffer;
    }

    public static void writeSizeAndMessage(MyChannel MyChannel, byte[] message) {
        byte[] sizeBytes = getMessageSize(message.length);
        byte[] buffer = new byte[sizeBytes.length + message.length];
        System.arraycopy(sizeBytes, 0, buffer, 0, sizeBytes.length);
        System.arraycopy(message, 0, buffer, sizeBytes.length, message.length);

        int bytesWritten = 0;
        while (bytesWritten < buffer.length) {
            int response = MyChannel.write(buffer, bytesWritten, buffer.length - bytesWritten);
            if (response == -1) {
                return;
            }
            bytesWritten += response;
        }

        if (VERBOSE) {
            System.out.println("Sent message: " + new String(message, 0, message.length));
        }
    }

    protected static Boolean VERBOSE = true;
    public static void main(String[] args) {
        // Create a new test object
        example test = new example();
        // Run the test
      //  test.test1();
     //   test.test2();
        test.test3();

     //   test.test4();
    }

    protected class EchoServer implements Runnable {
        protected MyBroker MyBroker;
        protected boolean isAccept;
        protected String MyBrokerName;
        protected int port;

        public EchoServer(MyBroker MyBroker, boolean isAccept, String MyBrokerName, int port) {
            this.MyBroker = MyBroker;
            this.isAccept = isAccept;
            this.MyBrokerName = MyBrokerName;
            this.port = port;
        }
        @Override
        public void run() {
            try {
                MyChannel serverMyChannel;
                int nbMessages = 0;
                if (isAccept) {
                    serverMyChannel = (MyChannel) MyBroker.accept(this.port);
                
                    while (nbMessages < 10) {
                        byte[] buffer = readSizeAndMessage(serverMyChannel);
    
                        writeSizeAndMessage(serverMyChannel, buffer);
    
                        nbMessages++;
                    }

                } else {
                    serverMyChannel = (MyChannel) MyBroker.connect(this.MyBrokerName, this.port);

                    while (nbMessages < 10) {

                        String message = "MyBroker " + MyBrokerName + " message number " + nbMessages;
                        writeSizeAndMessage(serverMyChannel, message.getBytes());

                        readSizeAndMessage(serverMyChannel);
    
                        nbMessages++;
                    }
                }
                
                serverMyChannel.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }

    public void test1() {
        MyBroker MyBroker = new MyBroker("MyBroker1");

        MyTask serverMyTask = new MyTask(MyBroker, new Runnable() {
            @Override
            public void run() {
                try {
                    // Listen on port 8080 for incoming connections
                    MyChannel serverMyChannel = (MyChannel) MyBroker.accept(8080);
                    if (serverMyChannel == null) {
                        return;
                    }
                    int nbMessages = 0;

                    while (nbMessages < 10) {
                        // Read message
                        byte[] buffer = readSizeAndMessage(serverMyChannel);


                        // Echo the message back to the client
                        writeSizeAndMessage(serverMyChannel, buffer);


                        nbMessages++;
                    }
                    serverMyChannel.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        MyTask clientMyTask = new MyTask(MyBroker, new Runnable() {
            @Override
            public void run() {
                try {
                    // Connect to the server on port 8080
                    MyChannel clientMyChannel = (MyChannel) MyBroker.connect("MyBroker1", 8080);
                    

                    int nbMessages = 0;

                    while (nbMessages < 10) {
                        String message = "Message " + nbMessages;

                        writeSizeAndMessage(clientMyChannel, message.getBytes());


                        // Read the message from the MyChannel

                        byte[] echoBuffer = readSizeAndMessage(clientMyChannel);

                        assert echoBuffer != null;
                        assert new String(echoBuffer, 0, echoBuffer.length).equals(message);
                        assert echoBuffer.length == message.length();

                        nbMessages++;
                    }

                    clientMyChannel.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            serverMyTask.join();
            clientMyTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void test2() {
        MyBroker MyBroker = new MyBroker("MyBroker1");
        MyBroker MyBroker2 = new MyBroker("MyBroker2");

        MyTask serverMyTask = new MyTask(MyBroker, new Runnable() {
            @Override
            public void run() {
                try {
                    // Listen on port 8080 for incoming connections
                    MyChannel serverMyChannel = (MyChannel) MyBroker.accept(8080);

                    int nbMessages = 0;

                    while (nbMessages < 10) {
                        // Read message
                        byte[] buffer = readSizeAndMessage(serverMyChannel);

                        // Echo the message back to the client
                        writeSizeAndMessage(serverMyChannel, buffer);

                        nbMessages++;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        MyTask clientMyTask = new MyTask(MyBroker2, new Runnable() {
            @Override
            public void run() {
                try {
                    // Connect to the server on port 8080
                    MyChannel clientMyChannel = (MyChannel) MyBroker2.connect("MyBroker1", 8080);
                    

                    int nbMessages = 0;

                    while (nbMessages < 10) {
                        String message = "Message " + nbMessages;

                        writeSizeAndMessage(clientMyChannel, message.getBytes());

                        // Read the message from the MyChannel

                        byte[] echoBuffer = readSizeAndMessage(clientMyChannel);

                        assert echoBuffer != null;
                        assert new String(echoBuffer, 0, echoBuffer.length).equals(message);
                        assert echoBuffer.length == message.length();

                        nbMessages++;
                    }

                    clientMyChannel.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            serverMyTask.join();
            clientMyTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void test3() {
        ArrayList<MyBroker> MyBrokers = new ArrayList<MyBroker>();
        ArrayList<MyTask> MyTasks = new ArrayList<MyTask>();

        for (int i = 0; i < 1000; i++) {
            MyBroker MyBroker = new MyBroker("MyBroker" + i);
            MyBrokers.add(MyBroker);
            MyTask serverMyTask = new MyTask(MyBroker, new EchoServer(MyBroker, true, "MyBroker" + i, i));
            MyTask clientMyTask = new MyTask(MyBroker, new EchoServer(MyBroker, false, "MyBroker" + i, i));
            MyTasks.add(serverMyTask);
            MyTasks.add(clientMyTask);
        }

        for (MyTask MyTask : MyTasks) {
            try {
                MyTask.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void test4() {
        MyBroker MyBroker = new MyBroker("MyBroker1");

        MyTask serverMyTask = new MyTask(MyBroker, new Runnable() {
            @Override
            public void run() {
                try {
                    // Listen on port 8080 for incoming connections
                    MyChannel serverMyChannel = (MyChannel) MyBroker.accept(8080);

                    int nbMessages = 0;
                    try {
                        while (nbMessages < 10) {
                            // Read message
                            byte[] buffer = readSizeAndMessage(serverMyChannel);

                            // Echo the message back to the client
                            writeSizeAndMessage(serverMyChannel, buffer);

                            if (nbMessages == 5) {
                                serverMyChannel.disconnect();
                            }

                            nbMessages++;
                        }  
                    } catch (IllegalStateException e) {
                        if (VERBOSE) {
                            System.out.println("Server disconnected");
                        }
                        if(nbMessages != 6) {
                            throw e;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        MyTask clientMyTask = new MyTask(MyBroker, new Runnable() {
            @Override
            public void run() {
                try {
                    // Connect to the server on port 8080
                    MyChannel clientMyChannel = (MyChannel) MyBroker.connect("MyBroker1", 8080);
                    

                    int nbMessages = 0;

                    while (nbMessages < 6) {
                        String message = "Message " + nbMessages;

                        writeSizeAndMessage(clientMyChannel, message.getBytes());

                        // Read the message from the MyChannel

                        byte[] echoBuffer = readSizeAndMessage(clientMyChannel);

                        assert echoBuffer != null;
                        assert new String(echoBuffer, 0, echoBuffer.length).equals(message);
                        assert echoBuffer.length == message.length();

                        nbMessages++;
                    }

                    clientMyChannel.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            serverMyTask.join();
            clientMyTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}