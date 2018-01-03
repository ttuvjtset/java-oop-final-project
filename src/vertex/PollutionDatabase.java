package vertex;

import motors.BenzineMotor;
import motors.DieselMotor;
import motors.Motor;
import restrictions.Restriction;
import restrictions.RestrictionForBenzine;
import restrictions.RestrictionForDiesel;

import java.util.ArrayList;
import java.util.concurrent.atomic.DoubleAdder;


class PollutionDatabase {
    private ArrayList<Motor> motors;
    private DoubleAdder totalPollutionAmount;
    private DrivingRestrictions drivingRestrictions;


    PollutionDatabase(DrivingRestrictions drivingRestrictions) {
        this.drivingRestrictions = drivingRestrictions;
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

    double blockConditionMetForCars(Restriction restriction) throws InterruptedException {
        synchronized (this) {
            while (totalPollutionAmount.doubleValue() < restriction.getPollutionRestriction()) {
                wait();
            }

            if (restriction instanceof RestrictionForDiesel) {
                drivingRestrictions.setBlockedForDieselCars();
                System.out.println("!!!!!!!!!!!!!!!ZABLOKIROVANO dlja dizelja " + drivingRestrictions.isBlockedForDieselCars());
            } else if (restriction instanceof RestrictionForBenzine) {
                drivingRestrictions.setBlockedForBenzineCars();
                System.out.println("!!!!!!!!!!!!!!!ZABLOKIROVANO dlja benzina " + drivingRestrictions.isBlockedForBenzineCars());
            }

            return totalPollutionAmount.doubleValue();
        }
    }

    double blockConditionMetForBenzineCars() throws InterruptedException {
        synchronized (this) {
            while (totalPollutionAmount.doubleValue() < 500) {
                wait();
            }

            drivingRestrictions.setBlockedForBenzineCars();
            System.out.println("!!!!!!!!!!!!!!!ZABLOKIROVANO dlja benzina " + drivingRestrictions.isBlockedForBenzineCars());

            return totalPollutionAmount.doubleValue();
        }
    }

    void askPermissionToDrive(Motor motor) throws InterruptedException {
        synchronized (this) {
            if (motor instanceof BenzineMotor) {
                while (drivingRestrictions.isBlockedForBenzineCars()) {
                    System.out.println("BLOCKED for Benzine");
                    wait();
                }
                System.out.println(">>>UNBLOCKED for Benzine");
            }
            if (motor instanceof DieselMotor) {
                while (drivingRestrictions.isBlockedForDieselCars()) {
                    System.out.println("BLOCKED for Diesel");
                    wait();
                }
                System.out.println(">>>UNBLOCKED for Diesel " + drivingRestrictions.isBlockedForBenzineCars() + " "
                        + drivingRestrictions.isBlockedForDieselCars());
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


}
