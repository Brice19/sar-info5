package task3.abst;

public abstract class Task extends Thread {

	public abstract void post(Runnable r);

	public static Task task() {
		Task task = (Task) Thread.currentThread();
		return task;
	}

	public abstract void kill();

	public abstract boolean killed();
}
