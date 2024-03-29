package inspection;

import motors.BenzineMotor;
import motors.DieselMotor;
import motors.Motor;
import restrictions.DrivingRestrictionTable;
import restrictions.Restriction;
import restrictions.RestrictionForBenzine;
import restrictions.RestrictionForDiesel;

import java.util.ArrayList;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class PollutionDatabase {
    private ArrayList<Motor> motors;
    private DoubleAdder totalPollutionAmount;
    private DrivingRestrictionTable drivingRestrictionTable;


    public PollutionDatabase(DrivingRestrictionTable drivingRestrictionTable) {
        this.drivingRestrictionTable = drivingRestrictionTable;
        this.motors = new ArrayList<>();
        this.totalPollutionAmount = new DoubleAdder();
    }

    public DoubleAdder getTotalPollutionAmount() {
        return totalPollutionAmount;
    }

    public void removeMotor(Motor motor) {
        motors.remove(motor);
    }

    long getInternalCombustionMotorCount() {
        return motors.stream().filter(motor -> motor instanceof BenzineMotor || motor instanceof DieselMotor).count();
    }

    public void addPollutionAmount(Motor motor, double pollution) {
        synchronized (this) {
            firstCarRegistration(motor);

            totalPollutionAmount.add(pollution);
            System.out.println("Pollution added: + " + pollution
                    + "                           = " + totalPollutionAmount);
            notifyAll();
        }
    }

    public void firstCarRegistration(Motor motor) {
        if (!motors.contains(motor)) {
            motors.add(motor);
        }
    }

    double getTotalPollutionWhenRestrictionApplies(Restriction restriction) throws InterruptedException {
        synchronized (this) {
            while (pollutionIsUnderLimit(restriction)) {
                wait();
            }

            if (restriction instanceof RestrictionForDiesel) {
                drivingRestrictionTable.setBlockForDiesel();
            } else if (restriction instanceof RestrictionForBenzine) {
                drivingRestrictionTable.setBlockForBenzine();
            }

            return totalPollutionAmount.doubleValue();
        }
    }

    private boolean pollutionIsUnderLimit(Restriction restriction) {
        return totalPollutionAmount.doubleValue() < restriction.getPollutionRestriction();
    }

    public boolean wasFurtherDrivingCurrentlyBlocked(Motor motor) throws InterruptedException {
        synchronized (this) {
            boolean furtherDrivingWasBlocked = false;

            if (motor instanceof BenzineMotor) {
                while (drivingRestrictionTable.isBlockedForBenzine()) {
                    wait();
                    furtherDrivingWasBlocked = true;
                }
            }

            if (motor instanceof DieselMotor) {
                while (drivingRestrictionTable.isBlockedForDiesel()) {
                    wait();
                    furtherDrivingWasBlocked = true;
                }
            }

            return furtherDrivingWasBlocked;
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

    public Stream<Motor> filterMotorsByCondition(Predicate<? super Motor> motorCondition) {
        return motors.stream().filter(motorCondition);
    }

}
