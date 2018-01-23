package tyres;

public class Tyres {
    private volatile boolean brokenTyres = false;

    public boolean isBrokenTyres() {
        return brokenTyres;
    }

    public void setBrokenTyres() {
        this.brokenTyres = true;
    }

    public void fixBrokenTyres() {
        this.brokenTyres = false;
    }
}
