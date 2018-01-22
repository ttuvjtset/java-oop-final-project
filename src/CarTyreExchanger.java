import inspection.PollutionDatabase;
import map.Graph;
import map.Vertex;
import motors.ElectricMotor;
import motors.LemonadeMotor;
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
        if (currentIntersection.equals(carWithFlatTyres.getVertexWhereCarIsWaitingForRepair())){
            System.out.println("~~~~~ We are here!!!! at " + currentIntersection);

            if (carWithFlatTyres.getCarWithFlatTyres().getMotor() instanceof ElectricMotor ||
                    carWithFlatTyres.getCarWithFlatTyres().getMotor() instanceof LemonadeMotor) {
                System.out.println("/!/!/! change tyres /!/!/!");
                carWithFlatTyres.getCarWithFlatTyres().changeTyres(new FruitPasteTyres());
            }
            carWithFlatTyres.getCarWithFlatTyres().getTyres().fixBrokenTyres();
            try {
                flatTyreInformer.tyresWereChanged();
                System.out.println("&&&&&& Tyres changed");
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return false;
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
