import car.Car;
import car.CarTyreExchanger;
import inspection.FlatTyreInformer;
import inspection.Inspection;
import inspection.PollutionDatabase;
import map.BadRoad;
import map.Graph;
import map.Vertex;
import motors.BenzineMotor;
import motors.DieselMotor;
import motors.ElectricMotor;
import motors.LemonadeMotor;
import restrictions.DrivingRestrictionTable;
import restrictions.Restriction;
import restrictions.RestrictionForBenzine;
import restrictions.RestrictionForDiesel;
import service.CarService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final int FIRST_CAR_UNIQUE_ID = 1;
    private static final int TOTAL_THREAD_COUNT = 210;
    private static final int TIMEOUT_BEFORE_CREATING_CARS = 100;
    private static final int INTERNAL_COMBUSTION_ENGINE_CARS = 30;
    private static final int ECO_ENGINE_CARS = 10;

    public static void main(String[] args) throws InterruptedException {
        Vertex vertex1 = new Vertex(1);
        Vertex vertex2 = new Vertex(2);
        Vertex vertex3 = new Vertex(3);
        Vertex vertex4 = new Vertex(4);
        Vertex vertex5 = new Vertex(5);
        Vertex vertex6 = new Vertex(6);
        Vertex vertex7 = new Vertex(7);
        Vertex vertex8 = new Vertex(8);
        Vertex vertex9 = new Vertex(9);
        Vertex vertex10 = new Vertex(10);
        Vertex vertex11 = new Vertex(11);
        Vertex vertex12 = new Vertex(12);
        Vertex vertex13 = new Vertex(13);
        Vertex vertex14 = new Vertex(14);
        Vertex vertex15 = new Vertex(15);
        Vertex vertex16 = new Vertex(16);

        Graph graph = new Graph();

        graph.addVertex(vertex1);
        graph.addVertex(vertex2);
        graph.addVertex(vertex3);
        graph.addVertex(vertex4);
        graph.addVertex(vertex5);
        graph.addVertex(vertex6);
        graph.addVertex(vertex7);
        graph.addVertex(vertex8);
        graph.addVertex(vertex9);
        graph.addVertex(vertex10);
        graph.addVertex(vertex11);
        graph.addVertex(vertex12);
        graph.addVertex(vertex13);
        graph.addVertex(vertex14);
        graph.addVertex(vertex15);
        graph.addVertex(vertex16);

        DrivingRestrictionTable drivingRestrictionTable = new DrivingRestrictionTable();
        PollutionDatabase pollutionDatabase = new PollutionDatabase(drivingRestrictionTable);
        Restriction restrictionForBenzine = new RestrictionForBenzine();
        Restriction restrictionForDiesel = new RestrictionForDiesel();

        FlatTyreInformer flatTyreInformer = new FlatTyreInformer();

        AtomicInteger uniqueCarIDs = new AtomicInteger(FIRST_CAR_UNIQUE_ID);

        ArrayList<Vertex> carServiceIntersections = new ArrayList<>(Arrays.asList(vertex2, vertex12, vertex5, vertex8));
        ArrayList<CarService> carServices = new ArrayList<>(Arrays.asList(new CarService(pollutionDatabase),
                new CarService(pollutionDatabase),
                new CarService(pollutionDatabase),
                new CarService(pollutionDatabase)));

        addEdges(graph);

        System.out.println(graph.getAdjList());
        System.out.println(graph.getEdges());

        BadRoad badRoad1 = new BadRoad(vertex2, vertex3);
        BadRoad badRoad2 = new BadRoad(vertex7, vertex6);
        ArrayList<BadRoad> badRoads = new ArrayList<>(Arrays.asList(badRoad1, badRoad2));

        PollutionDatabaseView pollutionDatabaseView = new PollutionDatabaseView();


        ExecutorService executor = Executors.newFixedThreadPool(TOTAL_THREAD_COUNT);
        executor.submit(new Bird(pollutionDatabaseView, pollutionDatabase));
        executor.submit(new Inspection(pollutionDatabase, drivingRestrictionTable, restrictionForBenzine));
        executor.submit(new Inspection(pollutionDatabase, drivingRestrictionTable, restrictionForDiesel));

        Thread.sleep(TIMEOUT_BEFORE_CREATING_CARS);

        for (int carsCount = 0; carsCount < INTERNAL_COMBUSTION_ENGINE_CARS; carsCount++) {
            executor.submit(new Car(uniqueCarIDs, graph, vertex14, badRoads, new BenzineMotor(), carServiceIntersections,
                    carServices, pollutionDatabase, flatTyreInformer));
            executor.submit(new Car(uniqueCarIDs, graph, vertex15, badRoads, new BenzineMotor(), carServiceIntersections,
                    carServices, pollutionDatabase, flatTyreInformer));
            executor.submit(new Car(uniqueCarIDs, graph, vertex16, badRoads, new BenzineMotor(), carServiceIntersections,
                    carServices, pollutionDatabase, flatTyreInformer));
            executor.submit(new Car(uniqueCarIDs, graph, vertex14, badRoads, new DieselMotor(), carServiceIntersections,
                    carServices, pollutionDatabase, flatTyreInformer));
            executor.submit(new Car(uniqueCarIDs, graph, vertex15, badRoads, new DieselMotor(), carServiceIntersections,
                    carServices, pollutionDatabase, flatTyreInformer));
            executor.submit(new Car(uniqueCarIDs, graph, vertex16, badRoads, new DieselMotor(), carServiceIntersections,
                    carServices, pollutionDatabase, flatTyreInformer));
        }

        for (int carsCount = 0; carsCount < ECO_ENGINE_CARS; carsCount++) {
            executor.submit(new Car(uniqueCarIDs, graph, vertex1, badRoads, new LemonadeMotor(), carServiceIntersections,
                    carServices, pollutionDatabase, flatTyreInformer));
            executor.submit(new Car(uniqueCarIDs, graph, vertex1, badRoads, new ElectricMotor(), carServiceIntersections,
                    carServices, pollutionDatabase, flatTyreInformer));
        }

        executor.submit(new CarTyreExchanger(graph, vertex1, flatTyreInformer));
    }

    private static void addEdges(Graph graph) {
        graph.addEdge(13, 14);
        graph.addEdge(14, 12);
        graph.addEdge(12, 15);
        graph.addEdge(15, 11);
        graph.addEdge(11, 16);
        graph.addEdge(16, 10);

        graph.addEdge(13, 12);
        graph.addEdge(12, 11);
        graph.addEdge(11, 10);
        graph.addEdge(10, 9);
        graph.addEdge(9, 8);
        graph.addEdge(8, 13);

        graph.addEdge(8, 7);
        graph.addEdge(6, 9);
        graph.addEdge(5, 10);
        graph.addEdge(4, 11);
        graph.addEdge(3, 12);
        graph.addEdge(2, 13);

        graph.addEdge(2, 7);
        graph.addEdge(7, 6);
        graph.addEdge(6, 5);
        graph.addEdge(5, 4);
        graph.addEdge(4, 3);
        graph.addEdge(3, 2);

        graph.addEdge(3, 1);
        graph.addEdge(4, 1);
        graph.addEdge(5, 1);
        graph.addEdge(6, 1);
        graph.addEdge(7, 1);
        graph.addEdge(2, 1);
    }
}
