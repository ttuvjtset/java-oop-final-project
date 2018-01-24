package car;

import inspection.FlatTyreInformer;
import map.Graph;
import map.Vertex;
import motors.ElectricMotor;
import motors.LemonadeMotor;
import tyres.TyresFruitPaste;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

public class CarTyreExchanger implements Runnable {

    private static final int DRIVE_TIME_UPPER_LIMIT = 200;
    private static final int DRIVE_TIME_LOWER_LIMIT = 30;

    private Graph graph;
    private Vertex startVertex;
    private FlatTyreInformer flatTyreInformer;
    private Vertex currentIntersection;

    public CarTyreExchanger(Graph graph, Vertex startVertex, FlatTyreInformer flatTyreInformer) {
        this.graph = graph;
        this.startVertex = startVertex;
        this.flatTyreInformer = flatTyreInformer;
    }

    private Optional<Vertex> getRandom(Collection<Vertex> adjVertices) {
        return adjVertices.stream()
                .skip((int) (adjVertices.size() * Math.random()))
                .findFirst();
    }

    @Override
    public void run() {
        driveCarToNextRandomIntersectionFromThe(startVertex);

        while (!Thread.interrupted()) {

            try {
                System.out.println(">>> TYRE EXCHANGER CAR >>> waiting for car with flat tyres at " +
                        currentIntersection);

                Optional<CarWithFlatTyres> carWithFlatTyres = Optional.of(flatTyreInformer.getCarWithFlatTyres());

                if (carWithFlatTyres.isPresent()) {
                    System.out.println(">>> TYRE EXCHANGER CAR >>> car "
                            + carWithFlatTyres.get().getCarWithFlatTyres().getCarMotorTypeAndID()
                            + " found at "
                            + carWithFlatTyres.get().getVertexWhereCarIsWaitingForRepair()
                            + "; we are at " + currentIntersection);

                    while (true) {
                        if (checkIfRightIntersectionAndFixTyres(carWithFlatTyres.get())) break;

                        driveCarToNextRandomIntersectionFromThe(currentIntersection);

                        System.out.println(">>> TYRE EXCHANGER CAR >>> currently at " + currentIntersection + "; " +
                                "Car with flat tyres is at "
                                + carWithFlatTyres.get().getVertexWhereCarIsWaitingForRepair());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkIfRightIntersectionAndFixTyres(CarWithFlatTyres carWithFlatTyres) {
        if (currentIntersection.equals(carWithFlatTyres.getVertexWhereCarIsWaitingForRepair())) {
            System.out.println(">>> TYRE EXCHANGER CAR >>> is on place at " + currentIntersection);

            if (carWithFlatTyres.getCarWithFlatTyres().getMotor() instanceof ElectricMotor ||
                    carWithFlatTyres.getCarWithFlatTyres().getMotor() instanceof LemonadeMotor) {
                System.out.println(">>> TYRE EXCHANGER CAR >>> installing TyresFruitPaste for eco friendly car");
                carWithFlatTyres.getCarWithFlatTyres().changeTyres(new TyresFruitPaste());
            }

            carWithFlatTyres.getCarWithFlatTyres().getTyres().fixBrokenTyres();

            try {
                flatTyreInformer.tyresWereChanged();
                System.out.println(">>> TYRE EXCHANGER CAR >>> tyres changed");
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    private void driveCarToNextRandomIntersectionFromThe(Vertex startVertex) {
        Optional<Vertex> getNextRandomVertex = getRandom(graph.getAdjVertices(startVertex));
        getNextRandomVertex.ifPresent(vertex -> currentIntersection = vertex);

        try {
            System.out.println(">>> TYRE EXCHANGER CAR >>> driving : " + startVertex + " -> " + currentIntersection);
            Thread.sleep(getStreetDriveTime());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getStreetDriveTime() {
        return new Random().nextInt(DRIVE_TIME_UPPER_LIMIT - DRIVE_TIME_LOWER_LIMIT) + DRIVE_TIME_LOWER_LIMIT;
    }
}
