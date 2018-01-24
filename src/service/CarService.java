package service;

import car.Car;
import inspection.PollutionDatabase;
import motors.LemonadeMotor;
import motors.Motor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class CarService {
    private BlockingQueue<Car> queue;
    private PollutionDatabase pollutionDatabase;

    public CarService(PollutionDatabase pollutionDatabase) {
        this.pollutionDatabase = pollutionDatabase;
        queue = new ArrayBlockingQueue<>(1);
    }

    public void addCar(Car car) throws InterruptedException {
        System.out.println("$$$ entering queue " + car.getCarMotorTypeAndID() + "$$$");
        queue.put(car);
    }

    public void removeCar() throws InterruptedException {
        queue.take();
    }

    public void changeMotorAndReregister(Car car) {
        System.out.println(queue.contains(car));
        if (queue.contains(car)) {
            pollutionDatabase.removeMotor(car.getMotor());

            Motor newEcoFriendlyMotor = new LemonadeMotor();
            car.changeMotor(newEcoFriendlyMotor);
            pollutionDatabase.firstCarRegistration(newEcoFriendlyMotor);
        }
    }
}
