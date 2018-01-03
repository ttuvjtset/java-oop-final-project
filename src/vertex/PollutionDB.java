package vertex;

import motors.DieselMotor;
import motors.Motor;

import java.util.ArrayList;
import java.util.concurrent.atomic.DoubleAdder;


class PollutionDB {
    private ArrayList<Motor> motors;
    private DoubleAdder totalPollutionAmount;
    private Block block;

    PollutionDB(Block block) {
        this.block = block;
        this.motors = new ArrayList<>();
        this.totalPollutionAmount = new DoubleAdder();
    }

    void addPollutionAmount(Motor motor, double pollution) {

        synchronized (this) {
            if (!motors.contains(motor)) {
                motors.add(motor);
            }

            totalPollutionAmount.add(pollution);
            System.out.println("ADDED:" + totalPollutionAmount.doubleValue());

            notifyAll();
        }
    }

    double blockConditionMet() throws InterruptedException {
        synchronized (this) {
            while (totalPollutionAmount.doubleValue() <= 400) {
                wait();
            }
            if (totalPollutionAmount.doubleValue() > 400 && totalPollutionAmount.doubleValue() <= 500) {
                block.setBlockedForDieselCars();
            }
            if (totalPollutionAmount.doubleValue() > 500) {
                block.setBlockedForDieselCars();
                block.setBlockedForBenzineCars();
            }
            return totalPollutionAmount.doubleValue();
        }
    }

    void askPermissionToDrive(Motor motor) throws InterruptedException {
        synchronized (this) {
            while (block.isBlockedForBenzineCars()) {
                System.out.println("BLOCKED");
                wait();
            }
        }
    }

    void informAboutUnblock() {
        synchronized (this) {
            notifyAll();
        }
    }

    void resetPollutionCounter() {
        totalPollutionAmount.reset();
    }

    private boolean blockingCondition(Motor motor) {
        return (totalPollutionAmount.doubleValue() > 100 && totalPollutionAmount.doubleValue() < 200)
                && motor instanceof DieselMotor;
    }
}
