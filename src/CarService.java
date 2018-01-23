import inspection.PollutionDatabase;
import motors.LemonadeMotor;
import motors.Motor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


class CarService {
    private BlockingQueue<Car> queue;
    private Car car;
    private PollutionDatabase pollutionDatabase;

    CarService(PollutionDatabase pollutionDatabase) {
        this.pollutionDatabase = pollutionDatabase;
        queue = new ArrayBlockingQueue<>(1);
    }

    void addCar(Car car) throws InterruptedException {
        this.car = car;
        System.out.println("$$$ entering queue " + car.getCarMotorTypeAndID() + "$$$");
        queue.put(car);
    }

    void removeCar() throws InterruptedException {
        queue.take();
    }

    void changeMotorAndReregister() {
        pollutionDatabase.removeMotor(car.getMotor());

        Motor newEcoFriendlyMotor = new LemonadeMotor();
        car.changeMotor(newEcoFriendlyMotor);
        pollutionDatabase.firstCarRegistration(newEcoFriendlyMotor);
    }

}
