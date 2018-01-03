import inspection.PollutionDatabase;
import map.Graph;
import map.Vertex;
import motors.Motor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Car implements Runnable {
    private String carID;
    private Graph graph;
    private Vertex startVertex;
    private Motor motor;
    private ArrayList<Vertex> carServiceIntersections;
    private ArrayList<CarService> carServices;
    private PollutionDatabase pollutionDatabase;
    private Vertex currentIntersection;
    private double pollution;
    private boolean needToChangeAMotorAtService;

    Car(AtomicInteger id, Graph graph, Vertex startVertex, Motor motor, ArrayList<Vertex> carServiceIntersections,
        ArrayList<CarService> carServices, PollutionDatabase pollutionDatabase) {
        this.carID = String.valueOf(id.getAndIncrement());
        this.graph = graph;
        this.startVertex = startVertex;
        this.motor = motor;
        this.carServiceIntersections = carServiceIntersections;
        this.carServices = carServices;
        this.pollutionDatabase = pollutionDatabase;
        this.pollution = 0;
        this.needToChangeAMotorAtService = false;
    }

    public Motor getMotor() {
        return motor;
    }

    public void changeMotor(Motor motor) {
        this.motor = motor;
    }

    private Optional<Vertex> getRandom(Collection<Vertex> adjVertices) {
        return adjVertices.stream()
                .skip((int) (adjVertices.size() * Math.random()))
                .findFirst();
    }

    @Override
    public void run() {
        pollutionDatabase.firstCarRegistration(motor);
        driveCarToNextRandomIntersectionFromThe(startVertex);

        int intersectionCounter = 1;
        int waitingTimesBecauseOfNonEcoFriendlyMotor = 0;

        while (!Thread.interrupted()) {

            if (intersectionCounter % 5 == 0) {
                pollutionDatabase.addPollutionAmount(motor, pollution);
                pollution = 0;
            }

            if (intersectionCounter % 7 == 0) {
                try {
                    System.out.println(motor.getMotorType() + carID + "Asking permission to continue driving");
                    boolean furtherDrivingBlockedBecauseOfMotor = pollutionDatabase.
                            isFurtherDrivingCurrentlyAllowed(motor);

                    if (furtherDrivingBlockedBecauseOfMotor) {
                        waitingTimesBecauseOfNonEcoFriendlyMotor++;
                        System.out.println(">>> Waiting Times Because Of Motor Counter: " + motor.getMotorType()
                                + carID + " " + waitingTimesBecauseOfNonEcoFriendlyMotor);

                        if (carOwnerDecidesToChangeMotorToEcoFriendly(waitingTimesBecauseOfNonEcoFriendlyMotor)) {
                            needToChangeAMotorAtService = true;
                            System.out.println(motor.getMotorType() + carID + " !!!!!!!!!!!!! decided to change Motor");
                        }
                    }
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

    private boolean carOwnerDecidesToChangeMotorToEcoFriendly(int waitingTimesBecauseOfMotorCounter) {
        return waitingTimesBecauseOfMotorCounter > 2 && probabilityOneSixth();
    }

    private boolean probabilityOneSixth() {
        return new Random().nextInt(6) == 0;
    }

    private void doCarService() {
        CarService carService = carServices.get(carServiceIntersections.indexOf(currentIntersection));
        try {
            carService.addCar(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("$$$ Beginning service works for " + motor.getMotorType() + carID + " $$$");

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (needToChangeAMotorAtService) {
            carService.changeMotorAndReregister();
            needToChangeAMotorAtService = false;
            System.out.println("MOTOR CHANGED!!");
        }

        System.out.println("$$$ End of service works for + " + motor.getMotorType() + carID + " $$$");

        try {
            carService.removeCar();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean currentIntersectionIsACarService() {
        return carServiceIntersections.contains(currentIntersection);
    }

    private void driveCarToNextRandomIntersectionFromThe(Vertex startVertex) {
        Optional<Vertex> getNextRandomVertex = getRandom(graph.getAdjVertices(startVertex));
        getNextRandomVertex.ifPresent(vertex -> currentIntersection = vertex);

        try {
            System.out.println(motor.getMotorType() + carID + " Riding from " + startVertex + " to " + currentIntersection);
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
