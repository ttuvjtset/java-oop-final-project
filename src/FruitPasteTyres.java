/**
 * Created by root on 22.01.2018.
 */
public class FruitPasteTyres extends Tyres {
    @Override
    public boolean isBrokenTyres() {
        return false;
    }

    @Override
    public void setBrokenTyres(){
        this.brokenTyres = false;
    }

    volatile boolean brokenTyres = false;
}
