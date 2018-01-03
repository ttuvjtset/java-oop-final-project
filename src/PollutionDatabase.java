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

    long getInternalCombustionMotorCount() {
        return motors.stream().filter(motor -> motor instanceof BenzineMotor || motor instanceof DieselMotor).count();
    }

    void addPollutionAmount(Motor motor, double pollution) {
        synchronized (this) {
            if (!motors.contains(motor)) {
                motors.add(motor);
            }

            totalPollutionAmount.add(pollution);
            System.out.println("Pollution added: + " + pollution
                    + "                           = " + totalPollutionAmount);
            notifyAll();
        }
    }

    double getTotalPollutionWhenRestrictionApplies(Restriction restriction) throws InterruptedException {
        synchronized (this) {
            while (pollutionIsUnderLimit(restriction)) {
                wait();
            }

            if (restriction instanceof RestrictionForDiesel) {
                drivingRestrictions.setBlockForDiesel();
            } else if (restriction instanceof RestrictionForBenzine) {
                drivingRestrictions.setBlockForBenzine();
            }

            return totalPollutionAmount.doubleValue();
        }
    }

    private boolean pollutionIsUnderLimit(Restriction restriction) {
        return totalPollutionAmount.doubleValue() < restriction.getPollutionRestriction();
    }

    void askPermissionToContinueDriving(Motor motor) throws InterruptedException {
        synchronized (this) {
            if (motor instanceof BenzineMotor) {
                while (drivingRestrictions.isBlockedForBenzine()) {
                    wait();
                }
            }

            if (motor instanceof DieselMotor) {
                while (drivingRestrictions.isBlockedForDiesel()) {
                    wait();
                }
            }
        }
    }

    void informAboutReleasedRestrictions() {
        synchronized (this) {
            notifyAll();
        }
    }

    void resetPollutionCounter(double pollutionAmountAfterReset) {
        synchronized (this) {
            totalPollutionAmount.reset();
            totalPollutionAmount.add(pollutionAmountAfterReset);
        }
    }
}
