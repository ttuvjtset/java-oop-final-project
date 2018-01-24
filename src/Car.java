import inspection.PollutionDatabase;
import map.Graph;
import map.Vertex;
import motors.Motor;
import tyres.FruitPasteTyres;
import tyres.Tyres;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class Car implements Runnable {
    private String carID;
    private Graph graph;
    private Vertex startVertex;
    private ArrayList<BadRoad> badRoads;
    private Motor motor;
    private ArrayList<Vertex> carServiceIntersections;
    private ArrayList<CarService> carServices;
    private PollutionDatabase pollutionDatabase;
    private FlatTyreInformer flatTyreInformer;
    private Vertex currentIntersection;
    private double pollution;
    private boolean needToChangeAMotorAtService;
    private Tyres tyres;

    public Car(AtomicInteger id, Graph graph, Vertex startVertex, ArrayList<BadRoad> badRoads, Motor motor,
        ArrayList<Vertex> carServiceIntersections,
        ArrayList<CarService> carServices, PollutionDatabase pollutionDatabase, FlatTyreInformer flatTyreInformer) {
        this.carID = String.valueOf(id.getAndIncrement());
        this.graph = graph;
        this.startVertex = startVertex;
        this.badRoads = badRoads;
        this.motor = motor;
        this.carServiceIntersections = carServiceIntersections;
        this.carServices = carServices;
        this.pollutionDatabase = pollutionDatabase;
        this.flatTyreInformer = flatTyreInformer;
        this.pollution = 0;
        this.needToChangeAMotorAtService = false;
        this.tyres = new Tyres();
    }

    public Tyres getTyres() {
        return tyres;
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
        //first street driving
        pollutionDatabase.firstCarRegistration(motor);
        driveCarToNextRandomIntersectionFromThe(startVertex);


        //intersection
        int intersectionCounter = 1;
        int waitingTimesBecauseOfNonEcoFriendlyMotor = 0;
        int drivenThroughBadStreetCounter = 0;

        while (!Thread.interrupted()) {

            if (intersectionCounter % 5 == 0) {
                pollutionDatabase.addPollutionAmount(motor, pollution);
                pollution = 0;
            }

            if (intersectionCounter % 7 == 0) {
                try {
                    System.out.println(getCarMotorTypeAndID() + " asking permission to continue driving");
                    boolean furtherDrivingBlockedBecauseOfMotor = pollutionDatabase.
                            wasFurtherDrivingCurrentlyBlocked(motor);

                    if (furtherDrivingBlockedBecauseOfMotor) {
                        waitingTimesBecauseOfNonEcoFriendlyMotor++;
                        System.out.println(getCarMotorTypeAndID() + " waiting times because of non eco motor counter: " +
                                + waitingTimesBecauseOfNonEcoFriendlyMotor);

                        if (carOwnerDecidesToChangeMotorToEcoFriendly(waitingTimesBecauseOfNonEcoFriendlyMotor)) {
                            needToChangeAMotorAtService = true;
                            System.out.println("!!!! " + getCarMotorTypeAndID() + " decided to change Motor !!!!");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (currentIntersectionIsACarService()) {
                doCarService();
            }

            //driving to next intersection
            Vertex drivingFromIntersection = currentIntersection;
            driveCarToNextRandomIntersectionFromThe(currentIntersection);
            Vertex drivingToIntersection = currentIntersection;

            Optional<BadRoad> wasTheStreetBad = badRoads.stream()
                    .filter(wasCurrentStreetBad(drivingFromIntersection, drivingToIntersection))
                    .findAny();

            if (wasTheStreetBad.isPresent()) {
                drivenThroughBadStreetCounter++;
                System.out.println(getCarMotorTypeAndID() + " Bad street Counter: "
                        + drivenThroughBadStreetCounter);
            }

            if (drivenThroughBadStreetCounter == 3 && !(tyres instanceof FruitPasteTyres)) {
                System.out.println(getCarMotorTypeAndID() + " !!!! TYRES BROKEN !!!!");
                tyres.setBrokenTyres();
                try {
                    flatTyreInformer.informAndAddToList(this, drivingToIntersection);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (tyres.isBrokenTyres()) {
                    try {
                        flatTyreInformer.checkIfTyresAreChanged();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(getCarMotorTypeAndID() + " !!!! TYRES FIXED !!!!");

                //workaround
                drivenThroughBadStreetCounter = 0;
            }

            intersectionCounter++;
        }
    }

    public String getCarMotorTypeAndID() {
        return motor.getMotorType() + carID;
    }

    private Predicate<BadRoad> wasCurrentStreetBad(Vertex drivingFromIntersection, Vertex drivingToIntersection) {
        return badRoad -> (badRoad.getFirstIntersection().equals(drivingFromIntersection)
                && badRoad.getSecondIntersection().equals(drivingToIntersection)
                || (badRoad.getFirstIntersection().equals(drivingToIntersection)
                && badRoad.getSecondIntersection().equals(drivingFromIntersection)));
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

        System.out.println(getCarMotorTypeAndID() + " $$$ Beginning service $$$");

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (needToChangeAMotorAtService) {
            carService.changeMotorAndReregister(this);
            needToChangeAMotorAtService = false;
            System.out.println(getCarMotorTypeAndID() + " $$$ Motor changed $$$");
        }

        System.out.println(getCarMotorTypeAndID() + " $$$ End of service $$$");

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
        //System.out.println("%%%%% DEBUG:" + graph.getAdjVertices(startVertex) + "FROM" + startVertex);
        getNextRandomVertex.ifPresent(vertex -> currentIntersection = vertex);

        try {
            System.out.println(getCarMotorTypeAndID() + " Driving: " + startVertex + " -> " + currentIntersection);
            Thread.sleep(getStreetDriveTime());
            pollution += motor.getPollutionRatio();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void changeTyres(Tyres newTyres) {
        tyres = newTyres;
    }

    private int getStreetDriveTime() {
        return new Random().nextInt(200 - 30) + 30;
    }
}
