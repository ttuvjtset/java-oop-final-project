import inspection.PollutionDatabase;
import map.Graph;
import map.Vertex;
import motors.Motor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class CarTyreExchanger implements Runnable {
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

    CarTyreExchanger(AtomicInteger id, Graph graph, Vertex startVertex, ArrayList<BadRoad> badRoads, Motor motor,
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
        while (!Thread.interrupted()) {
            CarWithFlatTyres carWithFlatTyres = null;
            try {
                System.out.println("FLAT TYRES again here");
                carWithFlatTyres = flatTyreInformer.getCarWithFlatTyres();
                System.out.println("FLAT TYRES" + carWithFlatTyres);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        //first street driving
        //pollutionDatabase.firstCarRegistration(motor);



        //intersection
        //int intersectionCounter = 1;
        //int waitingTimesBecauseOfNonEcoFriendlyMotor = 0;
        //int drivenThroughBadStreetCounter = 0;


//            if (intersectionCounter % 5 == 0) {
//                pollutionDatabase.addPollutionAmount(motor, pollution);
//                pollution = 0;
//            }
//
//            if (intersectionCounter % 7 == 0) {
//                try {
//                    System.out.println(motor.getMotorType() + carID + "Asking permission to continue driving");
//                    boolean furtherDrivingBlockedBecauseOfMotor = pollutionDatabase.
//                            isFurtherDrivingCurrentlyBlocked(motor);
//
//                    if (furtherDrivingBlockedBecauseOfMotor) {
//                        waitingTimesBecauseOfNonEcoFriendlyMotor++;
//                        System.out.println(">>> Waiting Times Because Of Motor Counter: " + motor.getMotorType()
//                                + carID + " " + waitingTimesBecauseOfNonEcoFriendlyMotor);
//
//                        if (carOwnerDecidesToChangeMotorToEcoFriendly(waitingTimesBecauseOfNonEcoFriendlyMotor)) {
//                            needToChangeAMotorAtService = true;
//                            System.out.println(motor.getMotorType() + carID + " !!!!!!!!!!!!! decided to change Motor");
//                        }
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

//            if (currentIntersectionIsACarService()) {
//                doCarService();
//            }

            //driving to next intersection
            while (true) {
                driveCarToNextRandomIntersectionFromThe(startVertex);
                Vertex drivingFromIntersection = currentIntersection;
                driveCarToNextRandomIntersectionFromThe(currentIntersection);
                Vertex drivingToIntersection = currentIntersection;

                System.out.println("&&&&&& TO " + currentIntersection + " " + carWithFlatTyres.getVertexWhereCarIsWaitingForRepair());
                if (currentIntersection.equals(carWithFlatTyres.getVertexWhereCarIsWaitingForRepair())){
                    System.out.println("We are here!!!!");
                    try {
                        Thread.sleep(1000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

//            Optional<BadRoad> wasTheStreetBad = badRoads.stream()
//                    .filter(wasCurrentStreetBad(drivingFromIntersection, drivingToIntersection))
//                    .findAny();
//
//            if (wasTheStreetBad.isPresent()) {
//                drivenThroughBadStreetCounter++;
//                System.out.println(motor.getMotorType() + carID + "Bad street!!!!! Counter: "
//                        + drivenThroughBadStreetCounter);
//            }
//
//            if (drivenThroughBadStreetCounter == 3) {
//                flatTyreInformer.informAndAddToList(this, drivingToIntersection);
//            }
//
//            intersectionCounter++;
        }
    }

//    private Predicate<BadRoad> wasCurrentStreetBad(Vertex drivingFromIntersection, Vertex drivingToIntersection) {
//        return badRoad -> (badRoad.getFirstIntersection().equals(drivingFromIntersection)
//                && badRoad.getSecondIntersection().equals(drivingToIntersection)
//                || (badRoad.getFirstIntersection().equals(drivingToIntersection)
//                && badRoad.getSecondIntersection().equals(drivingFromIntersection)));
//    }

    private boolean carOwnerDecidesToChangeMotorToEcoFriendly(int waitingTimesBecauseOfMotorCounter) {
        return waitingTimesBecauseOfMotorCounter > 2 && probabilityOneSixth();
    }

    private boolean probabilityOneSixth() {
        return new Random().nextInt(6) == 0;
    }

//    private void doCarService() {
//        CarService carService = carServices.get(carServiceIntersections.indexOf(currentIntersection));
//        try {
//            carService.addCar(this);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("$$$ Beginning service works for " + motor.getMotorType() + carID + " $$$");
//
//        try {
//            Thread.sleep(50);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        if (needToChangeAMotorAtService) {
//            carService.changeMotorAndReregister();
//            needToChangeAMotorAtService = false;
//            System.out.println("MOTOR CHANGED!!");
//        }
//
//        System.out.println("$$$ End of service works for + " + motor.getMotorType() + carID + " $$$");
//
//        try {
//            carService.removeCar();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean currentIntersectionIsACarService() {
//        return carServiceIntersections.contains(currentIntersection);
//    }

    private void driveCarToNextRandomIntersectionFromThe(Vertex startVertex) {
        Optional<Vertex> getNextRandomVertex = getRandom(graph.getAdjVertices(startVertex));
        getNextRandomVertex.ifPresent(vertex -> currentIntersection = vertex);

        try {
            System.out.println(motor.getMotorType() + carID + " >>>>>>>>> Riding from " + startVertex + " to " + currentIntersection);
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
