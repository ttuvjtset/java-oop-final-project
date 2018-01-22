import map.Vertex;

import java.util.ArrayList;


public class FlatTyreInformer {

    private ArrayList<Car> carsWithFlatTyres = new ArrayList<>();
    private ArrayList<Vertex> verticesWithCarsWithFlatTyres = new ArrayList<>();

    public CarWithFlatTyres getCarWithFlatTyres() throws InterruptedException {
        synchronized (this) {
            while (carsWithFlatTyres.isEmpty()) {
                wait();
            }

            return new CarWithFlatTyres(carsWithFlatTyres.remove(0), verticesWithCarsWithFlatTyres.remove(0));
        }
    }

    public void informAndAddToList(Car car, Vertex drivingToIntersection) throws InterruptedException {
        synchronized (this) {
            System.out.println("add");
            carsWithFlatTyres.add(car);
            verticesWithCarsWithFlatTyres.add(drivingToIntersection);
            notifyAll();
            wait();
        }
    }


}
