import inspection.Inspection;
import inspection.PollutionDatabase;
import map.Graph;
import map.Vertex;
import motors.BenzineMotor;
import motors.DieselMotor;
import restrictions.DrivingRestrictionTable;
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

        DrivingRestrictionTable drivingRestrictionTable = new DrivingRestrictionTable();
        PollutionDatabase pollutionDatabase = new PollutionDatabase(drivingRestrictionTable);
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

        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.submit(new Inspection(pollutionDatabase, drivingRestrictionTable, restrictionForBenzine));
        executor.submit(new Inspection(pollutionDatabase, drivingRestrictionTable, restrictionForDiesel));
        executor.submit(new Car("BENZINE1", graph, vertex1, new BenzineMotor(), carServices, pollutionDatabase));

        Thread.sleep(2000);

        executor.submit(new Car("DIESEL2 ", graph, vertex1, new DieselMotor(), carServices, pollutionDatabase));

    }
}
