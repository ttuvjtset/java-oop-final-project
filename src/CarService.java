import inspection.PollutionDatabase;
import motors.LemonadeMotor;
import motors.Motor;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;
import java.util.stream.Stream;


class CarService {
    private BlockingQueue<Car> queue;
    private ArrayList<Car> servicedCars;
    private Car car;
    private PollutionDatabase pollutionDatabase;

    CarService(PollutionDatabase pollutionDatabase) {
        this.pollutionDatabase = pollutionDatabase;
        queue = new ArrayBlockingQueue<>(1);
        servicedCars = new ArrayList<>();
    }

    void addCar(Car car) throws InterruptedException {
        this.car = car;
        queue.put(car);
        servicedCars.add(car);
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

    Stream<Car> filterServicedCarsByCondition(Predicate<Car> filterCondition) {
        return servicedCars.stream().filter(filterCondition);
    }

}


