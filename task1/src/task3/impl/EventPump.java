package task3.impl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventPump extends Thread {


    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();

     // Singleton instance of the EventPump
     private static final EventPump instance = new EventPump();

     private EventPump() {}

         // Static method to get the single instance of the EventPump
    public static EventPump getInstance() {
        return instance;
    }

    public void addTask(Runnable task) {
        taskQueue.add(task);
    }

    public void run() {
        while (true) {
            Runnable task = taskQueue.poll();
            if (task != null) {
                task.run();
            }
            // Optionally sleep to prevent tight looping if needed
        try {
            Thread.sleep(10);  // Adjust sleep time as necessary to reduce CPU usage when the queue is empty
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            break;
        }
        }
        
    }

    public void stopPump() {
        addTask(new ExitEvent());
    }



}
