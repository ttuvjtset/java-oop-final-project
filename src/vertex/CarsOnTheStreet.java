package vertex;

import motors.DieselMotor;
import motors.Motor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CarsOnTheStreet {
    private final List<Car> cars = new ArrayList<>();
    private double pollutionAmount;
    private HashMap<Car, Motor> carMotorDB = new HashMap<>();
    private final HashMap<Car, Double> carPollutionDB = new HashMap<>();

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

    public void updatePollutionData(Car car, double pollution) {
//        synchronized (carPollutionDB) {
            carPollutionDB.put(car, pollution);
//            notifyAll();
//        }

    }

    public void askPermissionToDrive(Car car) throws InterruptedException {
        //synchronized (carPollutionDB) {
            double totalPollution = carPollutionDB.entrySet()
                    .stream()
                    .mapToDouble(Map.Entry::getValue).sum();
            System.out.println(totalPollution);
//            System.out.println("ee");
            if (totalPollution > 15 && car.getMotor() instanceof DieselMotor) {
                wait();
            }
            //}
        //}
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
