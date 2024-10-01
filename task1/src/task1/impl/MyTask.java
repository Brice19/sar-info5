package task1.impl;

import task1.abst.Broker;
import task1.abst.Task;

/**
 * MyTask is a concrete implementation of the abstract Task class.
 * It associates a Broker and a Runnable with a unique UUID and
 * manages the execution of the Runnable on a separate thread.
 */
public class MyTask extends Task {

    // Unique identifier for this task
    private String uuid;

    // The broker associated with this task
    private Broker broker;

    // The runnable task to be executed
    private Runnable runnable;

    // The thread in which the runnable task will be executed
    private Thread thread;

    /**
     * Constructs a new MyTask instance with a specified Broker and Runnable.
     * The task is given a unique UUID and a new thread is created to run the task.
     *
     * @param b The Broker associated with this task
     * @param r The Runnable task to be executed
     */
    public MyTask(Broker b, Runnable r) {
        this.broker = b;
        this.runnable = r;
        this.uuid = java.util.UUID.randomUUID().toString();
        // Creates a new thread to execute the provided Runnable
        this.thread = new Thread(this.runnable, uuid);
        this.thread.start(); // Starts the thread
    }

    /**
     * Returns the Broker associated with the currently executing task.
     * This method casts the current thread to MyTask and retrieves its broker.
     *
     * @return The Broker associated with the current task
     * @throws ClassCastException if the current thread is not a MyTask instance
     */
    public static Broker getBroker() {
        // Retrieves the broker associated with the current thread's MyTask instance
        try {
            return ((MyTask) Thread.currentThread()).broker;
        } catch (ClassCastException e) {
            throw new RuntimeException("The current thread is not associated with a MyTask instance.");
        }
    }
}
