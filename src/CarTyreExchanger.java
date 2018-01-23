import map.Graph;
import map.Vertex;
import motors.ElectricMotor;
import motors.LemonadeMotor;
import motors.Motor;
import tyres.FruitPasteTyres;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class CarTyreExchanger implements Runnable {
    private String carID;
    private Graph graph;
    private Vertex startVertex;

    private Motor motor;

    private FlatTyreInformer flatTyreInformer;
    private Vertex currentIntersection;


    CarTyreExchanger(AtomicInteger id, Graph graph, Vertex startVertex, Motor motor, FlatTyreInformer flatTyreInformer) {
        this.carID = String.valueOf(id.getAndIncrement());
        this.graph = graph;
        this.startVertex = startVertex;

        this.motor = motor;

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
            CarWithFlatTyres carWithFlatTyres = null;
            try {
                System.out.println("~~~~ FLAT TYRES EXCHANGER SEARCHING FOR AUTO....");
                carWithFlatTyres = flatTyreInformer.getCarWithFlatTyres();
                System.out.println("~~~~ FLAT TYRES EXCHANGER: CAR FOUND " + carWithFlatTyres.getVertexWhereCarIsWaitingForRepair()
                        + " " + carWithFlatTyres.getCarWithFlatTyres()
                        + "; we are at " + currentIntersection);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (true) {
                if (checkIfRightIntersectionAndFixTyres(carWithFlatTyres)) break;

                driveCarToNextRandomIntersectionFromThe(currentIntersection);

                System.out.println("~~~~ DRIVING TO " + currentIntersection + "; Car is at "
                        + carWithFlatTyres.getVertexWhereCarIsWaitingForRepair());
            }
        }
    }

    private boolean checkIfRightIntersectionAndFixTyres(CarWithFlatTyres carWithFlatTyres) {
        if (currentIntersection.equals(carWithFlatTyres.getVertexWhereCarIsWaitingForRepair())) {
            System.out.println("~~~~~ We are here!!!! at " + currentIntersection);

            if (carWithFlatTyres.getCarWithFlatTyres().getMotor() instanceof ElectricMotor ||
                    carWithFlatTyres.getCarWithFlatTyres().getMotor() instanceof LemonadeMotor) {
                System.out.println("/!/!/! change tyres /!/!/!");
                carWithFlatTyres.getCarWithFlatTyres().changeTyres(new FruitPasteTyres());
            }
            carWithFlatTyres.getCarWithFlatTyres().getTyres().fixBrokenTyres();
            try {
                flatTyreInformer.tyresWereChanged();
                System.out.println("&&&&&& tyres.Tyres changed");
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
            System.out.println(motor.getMotorType() + carID + " >>>>>>>>> Riding from " + startVertex + " to " + currentIntersection);
            Thread.sleep(getStreetDriveTime());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getStreetDriveTime() {
        return new Random().nextInt(200 - 30) + 30;
    }
}
