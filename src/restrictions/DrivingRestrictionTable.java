package restrictions;

public class DrivingRestrictionTable {
    private volatile boolean drivingIsBlockedForBenzine = false;
    private volatile boolean drivingIsBlockedForDiesel = false;

    public boolean isBlockedForBenzine() {
        return drivingIsBlockedForBenzine;
    }

    public boolean isBlockedForDiesel() {
        return drivingIsBlockedForDiesel;
    }

    public void setBlockForBenzine() {
        drivingIsBlockedForBenzine = true;
    }

    public void setBlockForDiesel() {
        drivingIsBlockedForDiesel = true;
    }

    public void releaseBlockForBenzine() {
        drivingIsBlockedForBenzine = false;
    }

    public void releaseBlockForDiesel() {
        drivingIsBlockedForDiesel = false;
    }
}
