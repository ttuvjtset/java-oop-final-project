import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class CarServiceQueue {
    private BlockingQueue<Car> queue = new ArrayBlockingQueue<Car>(1);

    public void addCarToQueue(Car car) throws InterruptedException {
        queue.put(car);
    }

    void removeCarFromQueue() throws InterruptedException {
        queue.take();
    }
}


