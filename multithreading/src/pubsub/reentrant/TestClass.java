package pubsub.reentrant;

public class TestClass {

    public static void main(String[] args) {
        MyBlockingQueue queue = new MyBlockingQueue();

        Thread writer = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    double number = Math.random();
                    queue.write(number);
                    System.out.println("writer writes " + number);
                }
            }
        });

        Thread reader = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    double number = (double) queue.read();
                    System.out.println("Reader reads " + number);
                }
            }
        });

        writer.start();
        reader.start();
    }
}
