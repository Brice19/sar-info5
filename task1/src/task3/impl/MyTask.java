package task3.impl;

import task3.abst.Task;

public class MyTask extends Task {

    EventPump eventPump;
    boolean isRunning = true;
    Runnable task;

    public MyTask() {
        this.eventPump = EventPump.instance;
        this.task = null;
    }

    public void post(Runnable task) {
        if(isRunning) {
            this.task = task;
            eventPump.post(task);
        }
    }

    public void kill() {
        if(isRunning) {
            isRunning = false;
            eventPump.taskQueue.remove(task);
        }
    }

    @Override
    public boolean killed() {
        return !isRunning;
    }





    public void run() {
        System.out.println("MyTask.run()");
    }

}
