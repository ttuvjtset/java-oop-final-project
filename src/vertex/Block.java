package vertex;


class Block {
    private volatile boolean blockedBenzineCars = false;
    private volatile boolean blockedDieselCars = false;

    boolean isBlockedForBenzineCars() {
        return blockedBenzineCars;
    }

    boolean isBlockedForDieselCars() {
        return blockedBenzineCars;
    }

    void setBlockedForBenzineCars() {
        blockedBenzineCars = true;
    }

    void setBlockedForDieselCars() {
        blockedDieselCars = true;
    }

    void releaseAllBlocks() {
        blockedBenzineCars = false;
        blockedDieselCars = false;
    }
}
