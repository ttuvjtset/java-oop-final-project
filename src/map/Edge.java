package map;

/**
 * Code taken from:
 * https://gist.github.com/smddzcy/bf8fc17dedf4d40b0a873fc44f855a58
 * smddzcy
 */
public class Edge {
    private static final int DEFAULT_WEIGHT = 1;

    Vertex v1, v2;
    private int weight;

    Edge(Vertex v1, Vertex v2) {
        this(v1, v2, DEFAULT_WEIGHT);
    }

    private Edge(Vertex v1, Vertex v2, int weight) {
        super();
        this.v1 = v1;
        this.v2 = v2;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Edge)) return false;

        Edge _obj = (Edge) obj;
        return _obj.v1.equals(v1) && _obj.v2.equals(v2) &&
                _obj.weight == weight;
    }

    @Override
    public int hashCode() {
        int result = v1.hashCode();
        result = 31 * result + v2.hashCode();
        result = 31 * result + weight;
        return result;
    }
}