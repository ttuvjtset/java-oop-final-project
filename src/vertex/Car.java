package vertex;


import java.util.Collection;
import java.util.Optional;
import java.util.Random;

public class Car implements Runnable {
    private Graph graph;
    private Vertex entryPoint;
    private Vertex nextIntersection;

    Car(Graph graph, Vertex entryPoint) {
        this.graph = graph;
        this.entryPoint = entryPoint;
    }

    private Optional<Vertex> getRandom(Collection<Vertex> adjVertices) {
        return adjVertices.stream()
                .skip((int) (adjVertices.size() * Math.random()))
                .findFirst();
    }

    @Override
    public void run() {
        driveCarToRandomNextIntersectionFrom(entryPoint);

        int counter = 1;

        while (counter <= 100) {

            if (nextIntersection.getLabel() == 4) {
                System.out.println("Servicing");
            }

            driveCarToRandomNextIntersectionFrom(nextIntersection);

            counter++;
        }
    }

    private void driveCarToRandomNextIntersectionFrom(Vertex startVertex) {
        Optional<Vertex> getNextRandomVertex = getRandom(graph.getAdjVertices(startVertex));
        getNextRandomVertex.ifPresent(vertex -> nextIntersection = vertex);

        try {
            Thread.sleep(getStreetDriveTime());
            System.out.println("Riding from " + startVertex + " to " + nextIntersection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getStreetDriveTime() {
        return new Random().nextInt(200 - 30) + 30;
    }


}
