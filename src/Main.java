import inspection.Inspection;
import inspection.PollutionDatabase;
import map.Graph;
import map.Vertex;
import motors.BenzineMotor;
import motors.DieselMotor;
import motors.LemonadeMotor;
import restrictions.DrivingRestrictionTable;
import restrictions.Restriction;
import restrictions.RestrictionForBenzine;
import restrictions.RestrictionForDiesel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Graph graph = new Graph();

        DrivingRestrictionTable drivingRestrictionTable = new DrivingRestrictionTable();
        PollutionDatabase pollutionDatabase = new PollutionDatabase(drivingRestrictionTable);
        Restriction restrictionForBenzine = new RestrictionForBenzine();
        Restriction restrictionForDiesel = new RestrictionForDiesel();

        AtomicInteger uniqueCarIDs = new AtomicInteger(1);

        Vertex vertex1 = new Vertex(1);
        Vertex vertex2 = new Vertex(2);
        Vertex vertex3 = new Vertex(3);
        Vertex vertex4 = new Vertex(4);

        ArrayList<Vertex> carServiceIntersections = new ArrayList<>(Collections.singletonList(vertex4));
        ArrayList<CarService> carServices = new ArrayList<>(Collections.singletonList(new CarService(pollutionDatabase)));

        graph.addVertex(vertex1);
        graph.addVertex(vertex2);
        graph.addVertex(vertex3);
        graph.addVertex(vertex4);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(3, 2);
        graph.addEdge(3, 4);
        graph.addEdge(4, 2);

        System.out.println(graph.getAdjList());

        ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.submit(new Inspection(pollutionDatabase, drivingRestrictionTable, restrictionForBenzine));
        executor.submit(new Inspection(pollutionDatabase, drivingRestrictionTable, restrictionForDiesel));
        executor.submit(new Car(uniqueCarIDs, graph, vertex1, new BenzineMotor(), carServiceIntersections,
                carServices, pollutionDatabase));

        Thread.sleep(2000);

        executor.submit(new Car(uniqueCarIDs, graph, vertex1, new DieselMotor(), carServiceIntersections,
                carServices, pollutionDatabase));
        executor.submit(new Car(uniqueCarIDs, graph, vertex1, new LemonadeMotor(), carServiceIntersections,
                carServices, pollutionDatabase));
    }
}
