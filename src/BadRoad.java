import map.Vertex;


public class BadRoad {
    private Vertex firstIntersection;
    private Vertex secondIntersection;

    public BadRoad(Vertex firstIntersection, Vertex secondIntersection) {
        this.firstIntersection = firstIntersection;
        this.secondIntersection = secondIntersection;
    }

    public Vertex getFirstIntersection() {
        return firstIntersection;
    }

    public Vertex getSecondIntersection() {
        return secondIntersection;
    }
}
