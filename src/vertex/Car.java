package vertex;


import java.util.Collection;
import java.util.Optional;

public class Car implements Runnable {
    private Graph graph;
    private Vertex entryPoint;
    private Vertex nextIntersection;

    Car(Graph graph, Vertex entryPoint) {
        this.graph = graph;
        this.entryPoint = entryPoint;
    }

    private static <E> Optional<E> getRandom(Collection<E> e) {

        return e.stream()
                .skip((int) (e.size() * Math.random()))
                .findFirst();
    }

    @Override
    public void run() {
        int counter = 0;
        while (counter <= 100) {


            Optional<Vertex> getNextRandomVertex;
            if (counter == 0) {
                getNextRandomVertex = getRandom(graph.getAdjVertices(entryPoint));
            } else {
                if (nextIntersection.getLabel() == 4) {
                    System.out.println("Servicing");
                }
                getNextRandomVertex = getRandom(graph.getAdjVertices(nextIntersection));
            }


            if (getNextRandomVertex.isPresent()) {
                nextIntersection = getNextRandomVertex.get();
                System.out.println(nextIntersection);
            }
            try {
                Thread.sleep(100);
                System.out.println("Riding");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
        }
    }


}
