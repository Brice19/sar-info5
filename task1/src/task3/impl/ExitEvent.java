package task3.impl;

public class ExitEvent implements Runnable {

    @Override
    public void run() {
        System.out.println("Exiting the EventPump or closing connections.");
    }

}
