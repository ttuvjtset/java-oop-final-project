package vertex;

import motors.Motor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CarsOnTheStreet {
    private final List<Car> cars = new ArrayList<>();
    private double pollutionAmount;
    private HashMap<Car, Motor> carMotorDB = new HashMap<>();


    public List<Car> getCars() {
        return cars;
    }

    void addCar(Car car) {
        synchronized (cars) {
            cars.add(car);
        }
    }

    public void sendPollutionData(double carsPollution) {
        pollutionAmount += carsPollution;
        System.out.println(pollutionAmount);
    }

    public void updateMotorData(Car car, Motor motor) {
        carMotorDB.put(car, motor);
    }


//    Car popCar() throws InterruptedException {
//        synchronized (cars) {
//            Optional<Car> ticketInContainer = Optional.empty();
//            while (!ticketInContainer.isPresent()) {
//                ticketInContainer = cars.stream().findAny();
//                if (!ticketInContainer.isPresent()) {
//                    cars.wait();
//                }
//            }
//            cars.remove(ticketInContainer.get());
//            return ticketInContainer.get();
//        }
//    }
}
