
public class Tyres {
    public boolean isBrokenTyres() {
        return brokenTyres;
    }

    public void setBrokenTyres(){
        this.brokenTyres = true;
    }

    public void fixBrokenTyres(){
        this.brokenTyres = false;
    }

    volatile boolean brokenTyres = false;


}
