import map.Graph;
import map.Vertex;
import motors.Motor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

public class Car implements Runnable {
    private String s;
    private Graph graph;
    private Vertex startVertex;
    private Motor motor;
    private ArrayList<Vertex> carServices;
    private CarsOnTheStreet carsOnTheStreet;
    private PollutionDatabase pollutionDatabase;
    private Vertex currentIntersection;
    private double pollution = 0;

    Car(String s, Graph graph, Vertex startVertex, Motor motor, ArrayList<Vertex> carServices,
        CarsOnTheStreet carsOnTheStreet, PollutionDatabase pollutionDatabase) {
        this.s = s;
        this.graph = graph;
        this.startVertex = startVertex;
        this.motor = motor;
        this.carServices = carServices;
        this.carsOnTheStreet = carsOnTheStreet;
        this.pollutionDatabase = pollutionDatabase;
    }

    public Motor getMotor() {
        return motor;
    }

    private Optional<Vertex> getRandom(Collection<Vertex> adjVertices) {
        return adjVertices.stream()
                .skip((int) (adjVertices.size() * Math.random()))
                .findFirst();
    }

    @Override
    public void run() {
        carsOnTheStreet.addCar(this);
        driveCarToRandomNextIntersectionFrom(startVertex);

        int counter = 1;

        while (true) {
            if (counter % 5 == 0) {
                pollutionDatabase.addPollutionAmount(motor, pollution);
                System.out.println("Adding pollution" + pollution + " and nulling");
                pollution = 0;
            }

            if (counter % 7 == 0) {
                try {
                    System.out.println(s + " ======================Asking permission");
                    pollutionDatabase.askPermissionToDrive(motor);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (carServices.contains(currentIntersection)) {
                System.out.println("Servicing");
            }

            driveCarToRandomNextIntersectionFrom(currentIntersection);
            counter++;
        }
    }

    private void driveCarToRandomNextIntersectionFrom(Vertex startVertex) {
        Optional<Vertex> getNextRandomVertex = getRandom(graph.getAdjVertices(startVertex));
        getNextRandomVertex.ifPresent(vertex -> currentIntersection = vertex);

        try {
            System.out.println(s + " Riding from " + startVertex + " to " + currentIntersection);
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
