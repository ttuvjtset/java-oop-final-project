package vertex;


import motors.BenzineMotor;
import motors.DieselMotor;
import motors.Motor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Graph graph = new Graph();
        CarsOnTheStreet carsOnTheStreet = new CarsOnTheStreet();

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

        Motor motor = new BenzineMotor();
        executor.submit(new Car("1", graph, vertex1, motor, carServices, carsOnTheStreet));

        Thread.sleep(2000);
        Motor motor2 = new DieselMotor();
        executor.submit(new Car("2", graph, vertex1, motor2, carServices, carsOnTheStreet));

    }
}
