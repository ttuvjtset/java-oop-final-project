import map.Graph;
import map.Vertex;
import motors.BenzineMotor;
import motors.DieselMotor;
import restrictions.Restriction;
import restrictions.RestrictionForBenzine;
import restrictions.RestrictionForDiesel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Graph graph = new Graph();
        CarsOnTheStreet carsOnTheStreet = new CarsOnTheStreet();
        DrivingRestrictions drivingRestrictions = new DrivingRestrictions();
        PollutionDatabase pollutionDatabase = new PollutionDatabase(drivingRestrictions);
        Restriction restrictionForBenzine = new RestrictionForBenzine();
        Restriction restrictionForDiesel = new RestrictionForDiesel();

        Vertex vertex1 = new Vertex(1);
        Vertex vertex2 = new Vertex(2);
        Vertex vertex3 = new Vertex(3);
        Vertex vertex4 = new Vertex(4);

        ArrayList<Vertex> carServices = new ArrayList<>(Collections.singletonList(vertex4));


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

        //System.out.println(graph.getAdjVertices(vertex3));
        //System.out.println(graph.getAdjVertices(vertex4));

        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.submit(new Inspection(pollutionDatabase, drivingRestrictions, restrictionForBenzine));
        executor.submit(new Inspection(pollutionDatabase, drivingRestrictions, restrictionForDiesel));
        executor.submit(new Car("1BENZ ", graph, vertex1, new BenzineMotor(), carServices, carsOnTheStreet, pollutionDatabase));

        Thread.sleep(2000);

        executor.submit(new Car("2DIESEL", graph, vertex1, new DieselMotor(), carServices, carsOnTheStreet, pollutionDatabase));

    }
}