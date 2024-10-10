package task3.impl;

import java.util.LinkedList;

public class EventPump extends Thread {


    public LinkedList<Runnable> taskQueue;

     // Singleton instance of the EventPump
     public static EventPump instance;

     public EventPump() {
        taskQueue = new LinkedList<Runnable>();
     }

         // Static method to get the single instance of the EventPump
    static {
        instance = new EventPump();
        instance.setName("EventPump");
        instance.start();
    }

    public void addTask(Runnable task) {
        taskQueue.add(task);
    }

    public void run() {
        while (true) {
            Runnable task = taskQueue.poll();
            while(task != null) {
                task.run();
                task = taskQueue.poll();
            }
            // Optionally sleep to prevent tight looping if needed
        try {
            wait();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            break;
        }
        }
        
    }

    public synchronized void  post(Runnable task) {
        addTask(task);
      //  TODO
      notify();
    }

    public synchronized void stopPump() {
        instance.taskQueue.clear();
    }

    static void getInstance() {
		instance = new EventPump();
	}

    public synchronized void kill() {
        stopPump();
    }

    public synchronized boolean killed() {
        return instance.taskQueue.isEmpty();
    }

    public synchronized void restart() {
        instance.taskQueue.clear();
        notify();
    }

}
