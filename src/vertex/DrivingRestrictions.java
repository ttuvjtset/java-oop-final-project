package vertex;


class DrivingRestrictions {
    private volatile boolean blockedBenzineCars = false;
    private volatile boolean blockedDieselCars = false;

    boolean isBlockedForBenzineCars() {
        return blockedBenzineCars;
    }

    boolean isBlockedForDieselCars() {
        return blockedDieselCars;
    }

    void setBlockedForBenzineCars() {
        blockedBenzineCars = true;
    }

    void setBlockedForDieselCars() {
        blockedDieselCars = true;
    }

    void releaseBenzineBlock() {
        blockedBenzineCars = false;
    }

    void releaseDieselBlock() {
        blockedDieselCars = false;
    }
}
