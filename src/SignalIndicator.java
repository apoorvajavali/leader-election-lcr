import java.util.concurrent.Semaphore;

public class SignalIndicator {
    private static int counter;

    public Semaphore semaphore = new Semaphore(1);

    public void increment() {
        try {
            semaphore.acquire();
            counter++;
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void decrement() {
        try {
            semaphore.acquire();
            counter--;
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isRoundCompleted() {
        boolean isComplete = false;
        try {
            semaphore.acquire();
            isComplete = counter == 0;
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isComplete;
    }
}
