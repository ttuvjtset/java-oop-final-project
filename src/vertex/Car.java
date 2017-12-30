package vertex;


import motors.Motor;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

public class Car implements Runnable {
    private Graph graph;
    private Vertex startVertex;
    private Motor motor;
    private CarsOnTheStreet carsOnTheStreet;
    private Vertex currentIntersection;
    private double pollution = 0;

    Car(Graph graph, Vertex startVertex, Motor motor, CarsOnTheStreet carsOnTheStreet) {
        this.graph = graph;
        this.startVertex = startVertex;
        this.motor = motor;
        this.carsOnTheStreet = carsOnTheStreet;
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

        while (counter <= 100) {
            if (counter % 5 == 0) {
                carsOnTheStreet.sendPollutionData(pollution);
                pollution = 0;
            }

            if (currentIntersection instanceof VertexWithCarService) {
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
            System.out.println("Riding from " + startVertex + " to " + currentIntersection);
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
