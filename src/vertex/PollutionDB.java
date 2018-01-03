package vertex;

import motors.BenzineMotor;
import motors.DieselMotor;
import motors.Motor;

import java.util.ArrayList;
import java.util.concurrent.atomic.DoubleAdder;


class PollutionDB {
    private ArrayList<Motor> motors;
    private DoubleAdder totalPollutionAmount;
    private volatile Block block;
    Object object1 = new Object();
    Object object2 = new Object();

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

    double blockConditionMetForBenzineCars() throws InterruptedException {
        synchronized (this) {
            while (totalPollutionAmount.doubleValue() < 400) {
                wait();
            }
            if (totalPollutionAmount.doubleValue() >= 400 && totalPollutionAmount.doubleValue() <= 500) {
                block.setBlockedForDieselCars();
                System.out.println("!!!!!!!!!!!!!!!ZABLOKIROVANO dlja dizelja " + block.isBlockedForDieselCars());
            }
            if (totalPollutionAmount.doubleValue() > 500) {
                block.setBlockedForBenzineCars();
                System.out.println("!!!!!!!!!!!!!!!ZABLOKIROVANO dlja benzina " + block.isBlockedForBenzineCars());
            }
            return totalPollutionAmount.doubleValue();
        }
    }



    void askPermissionToDrive(Motor motor) throws InterruptedException {
        synchronized (this) {
            if (motor instanceof BenzineMotor) {
                while (block.isBlockedForBenzineCars()) {
                    System.out.println("BLOCKED for Benzine");
                    wait();
                }
                System.out.println(">>>UNBLOCKED for Benzine");
            }
            if (motor instanceof DieselMotor) {
                while (block.isBlockedForDieselCars()) {
                    System.out.println("BLOCKED for Diesel");
                    wait();
                }
                System.out.println(">>>UNBLOCKED for Diesel " + block.isBlockedForBenzineCars() + " "
                        + block.isBlockedForDieselCars());
            }
        }
    }

    void informAboutUnblock() {
        synchronized (this) {
            notifyAll();
        }
    }

    void resetPollutionCounter() {
        synchronized (this) {
            totalPollutionAmount.reset();
        }

    }

//    private boolean blockingCondition(Motor motor) {
//        return (totalPollutionAmount.doubleValue() > 100 && totalPollutionAmount.doubleValue() < 200)
//                && motor instanceof DieselMotor;
//    }
}
