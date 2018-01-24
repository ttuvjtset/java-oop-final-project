package car;

import map.Vertex;


public class CarWithFlatTyres {
    private Car carWithFlatTyres;
    private Vertex vertexWhereCarIsWaitingForRepair;

    public CarWithFlatTyres(Car carWithFlatTyres, Vertex vertexWhereCarIsWaitingForRepair) {
        this.carWithFlatTyres = carWithFlatTyres;
        this.vertexWhereCarIsWaitingForRepair = vertexWhereCarIsWaitingForRepair;
    }

    public Car getCarWithFlatTyres() {
        return carWithFlatTyres;
    }

    public Vertex getVertexWhereCarIsWaitingForRepair() {
        return vertexWhereCarIsWaitingForRepair;
    }
}
