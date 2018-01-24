package inspection;

import restrictions.DrivingRestrictionTable;
import restrictions.Restriction;
import restrictions.RestrictionForBenzine;
import restrictions.RestrictionForDiesel;

public class Inspection implements Runnable {

    private static final int INTERNAL_COMBUSTION_MOTOR_COUNT_THRESHOLD = 70;
    private static final double POLLUTION_RESET_RATIO_WHEN_OVER_LIMIT = 0.4;
    private static final int BLOCKING_TIME = 2000;

    private PollutionDatabase pollutionDatabase;
    private DrivingRestrictionTable drivingRestrictionTable;
    private Restriction restriction;

    public Inspection(PollutionDatabase pollutionDatabase, DrivingRestrictionTable drivingRestrictionTable,
                      Restriction restriction) {
        this.pollutionDatabase = pollutionDatabase;
        this.drivingRestrictionTable = drivingRestrictionTable;
        this.restriction = restriction;
    }

    @Override
    public void run() {
        int blockCounter = 1;

        while (!Thread.interrupted()) {
            double pollutionAmountAfterReset = 0;

            try {
                double pollutionWhenRestrictionApplies = pollutionDatabase.
                        getTotalPollutionWhenRestrictionApplies(restriction);
                if (tooManyCarsWithInternalCombustionMotors()) {
                    pollutionAmountAfterReset = pollutionWhenRestrictionApplies * POLLUTION_RESET_RATIO_WHEN_OVER_LIMIT;
                }
                System.out.println("====== PollutionWhenRestrictionApplies: " + pollutionWhenRestrictionApplies
                        + " ======");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("........" + restriction.getPollutionRestriction() + ".BLOCK" + blockCounter
                        + "......................" +
                        ".........Waiting 2000 ms..............");
                Thread.sleep(BLOCKING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (restriction instanceof RestrictionForDiesel) {
                drivingRestrictionTable.releaseBlockForDiesel();
            } else if (restriction instanceof RestrictionForBenzine) {
                drivingRestrictionTable.releaseBlockForBenzine();
            }

            System.out.println("====== Driving Restrictions Released ======");
            pollutionDatabase.resetPollutionCounter(pollutionAmountAfterReset);
            pollutionDatabase.informAboutReleasedRestrictions();

            System.out.println("........" + restriction.getPollutionRestriction() + ".UNBLOCK" + blockCounter
                    + "........................" +
                    "..................UNBLOCKED..............");

            blockCounter++;
        }
    }

    private boolean tooManyCarsWithInternalCombustionMotors() {
        return pollutionDatabase.getInternalCombustionMotorCount() >= INTERNAL_COMBUSTION_MOTOR_COUNT_THRESHOLD;
    }
}
