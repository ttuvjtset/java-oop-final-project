package tyres;


public class FruitPasteTyres extends Tyres {
    private volatile boolean brokenTyres = false;

    @Override
    public boolean isBrokenTyres() {
        return false;
    }

    @Override
    public void setBrokenTyres() {
        this.brokenTyres = false;
    }
}
