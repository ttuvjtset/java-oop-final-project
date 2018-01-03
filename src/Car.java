import inspection.PollutionDatabase;
import map.Graph;
import map.Vertex;
import motors.Motor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

public class Car implements Runnable {
    private String carName;
    private Graph graph;
    private Vertex startVertex;
    private Motor motor;
    private ArrayList<Vertex> carServices;
    private ArrayList<CarServiceQueue> carServiceQueues;
    private PollutionDatabase pollutionDatabase;
    private Vertex currentIntersection;
    private double pollution;

    Car(String carName, Graph graph, Vertex startVertex, Motor motor, ArrayList<Vertex> carServices,
        ArrayList<CarServiceQueue> carServiceQueues, PollutionDatabase pollutionDatabase) {
        this.carName = carName;
        this.graph = graph;
        this.startVertex = startVertex;
        this.motor = motor;
        this.carServices = carServices;
        this.carServiceQueues = carServiceQueues;
        this.pollutionDatabase = pollutionDatabase;
        this.pollution = 0;
    }

/*    public Motor getMotor() {
        return motor;
    }*/

    private Optional<Vertex> getRandom(Collection<Vertex> adjVertices) {
        return adjVertices.stream()
                .skip((int) (adjVertices.size() * Math.random()))
                .findFirst();
    }

    @Override
    public void run() {
        driveCarToNextRandomIntersectionFromThe(startVertex);

        int intersectionCounter = 1;

        while (!Thread.interrupted()) {
            if (intersectionCounter % 5 == 0) {
                pollutionDatabase.addPollutionAmount(motor, pollution);
                pollution = 0;
            }

            if (intersectionCounter % 7 == 0) {
                try {
                    System.out.println(carName + "Asking permission to continue driving");
                    pollutionDatabase.askPermissionToContinueDriving(motor);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (currentIntersectionIsACarService()) {
                doCarService();
            }

            driveCarToNextRandomIntersectionFromThe(currentIntersection);
            intersectionCounter++;
        }
    }

    private void doCarService() {
        CarServiceQueue carServiceQueue = carServiceQueues.get(carServices.indexOf(currentIntersection));
        try {
            carServiceQueue.addCarToQueue(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("$$$ Beginning service works for " + carName + " $$$");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("$$$ End of service works for + " + carName + " $$$");

        try {
            carServiceQueue.removeCarFromQueue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean currentIntersectionIsACarService() {
        return carServices.contains(currentIntersection);
    }

    private void driveCarToNextRandomIntersectionFromThe(Vertex startVertex) {
        Optional<Vertex> getNextRandomVertex = getRandom(graph.getAdjVertices(startVertex));
        getNextRandomVertex.ifPresent(vertex -> currentIntersection = vertex);

        try {
            System.out.println(carName + " Riding from " + startVertex + " to " + currentIntersection);
            Thread.sleep(getStreetDriveTime());
            pollution += motor.getPollutionRatio();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getStreetDriveTime() {
        return new Random().nextInt(200 - 30) + 30;
    }
}
