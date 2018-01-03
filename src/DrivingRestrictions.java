class DrivingRestrictions {
    private volatile boolean drivingIsBlockedForBenzine = false;
    private volatile boolean drivingIsBlockedForDiesel = false;

    boolean isBlockedForBenzine() {
        return drivingIsBlockedForBenzine;
    }

    boolean isBlockedForDiesel() {
        return drivingIsBlockedForDiesel;
    }

    void setBlockForBenzine() {
        drivingIsBlockedForBenzine = true;
    }

    void setBlockForDiesel() {
        drivingIsBlockedForDiesel = true;
    }

    void releaseBlockForBenzine() {
        drivingIsBlockedForBenzine = false;
    }

    void releaseBlockForDiesel() {
        drivingIsBlockedForDiesel = false;
    }
}
