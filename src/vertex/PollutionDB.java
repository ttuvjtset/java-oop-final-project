package vertex;

import motors.DieselMotor;
import motors.Motor;

import java.util.ArrayList;
import java.util.concurrent.atomic.DoubleAdder;


class PollutionDB {
    private ArrayList<Motor> motors;

//    public double getTotalPollutionAmount() throws InterruptedException {
//        synchronized (this) {
//            while (totalPollutionAmount.doubleValue() < 100 && totalPollutionAmount.doubleValue() > 200) {
//                System.out.println("BLOCKED nadzor");
//                wait();
//            }
//            return totalPollutionAmount.doubleValue();
//        }
//    }

    private DoubleAdder totalPollutionAmount;

    PollutionDB() {
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

    void askPermissionToDrive(Motor motor) throws InterruptedException {
        synchronized (this) {
            while (blockingCondition(motor)) {
                System.out.println("BLOCKED");

                wait();
            }
        }
    }

    private boolean blockingCondition(Motor motor) {
        return (totalPollutionAmount.doubleValue() > 100 && totalPollutionAmount.doubleValue() < 200)
                && motor instanceof DieselMotor;
    }
}
