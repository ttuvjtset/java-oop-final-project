package vertex;

import java.util.ArrayList;
import java.util.List;


public class CarsOnTheStreet {
    private final List<Car> cars = new ArrayList<>();
    private double pollutionAmount;

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
