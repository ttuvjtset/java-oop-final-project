import map.Vertex;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class FlatTyreInformer {
    final HashMap<Car, Vertex> carsWithFlatTyresAndTheirCoordinates = new HashMap<>();

    public void informAndAddToList(Car car, Vertex drivingToIntersection) {
        synchronized (carsWithFlatTyresAndTheirCoordinates) {
            carsWithFlatTyresAndTheirCoordinates.put(car, drivingToIntersection);
        }
    }

    public Car getCarWithFlatTyres() throws InterruptedException {
        synchronized (carsWithFlatTyresAndTheirCoordinates) {
            while (carsWithFlatTyresAndTheirCoordinates.isEmpty()) {
                wait();
            }
            Optional<Car> first = carsWithFlatTyresAndTheirCoordinates.entrySet().stream().map(Map.Entry::getKey)
                    .findFirst();
        }
        return null;
    }


}
