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


    void askPermissionToDrive(Motor motor) throws InterruptedException {
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
