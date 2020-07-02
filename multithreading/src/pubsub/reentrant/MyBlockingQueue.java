package pubsub.reentrant;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue {

    private List<Object> queue = new LinkedList<>();
    private static final int MAX_SIZE = 10;

    private ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition empty = lock.newCondition();

    public Object read() {
        this.lock.lock();
        Object object = null;
        try {
            while (this.queue.size() == 0) {
                System.out.println("Queue is empty -- reader waiting");
                this.empty.await();
            }
            this.notEmpty.signal();
            object = this.queue.get(0);
            this.queue.remove(object);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.lock.unlock();
        }

        return object;
    }

    public void write (Object data) {
        this.lock.lock();
        try {
            while (this.queue.size() == MAX_SIZE) {
                System.out.println("Queue is full -- writer waiting");
                this.notEmpty.await();
            }
            this.empty.signal();
            this.queue.add(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.lock.unlock();
        }
    }
}
