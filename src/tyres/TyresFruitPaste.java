package tyres;


public class TyresFruitPaste extends Tyres {
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
