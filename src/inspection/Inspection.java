package inspection;

import restrictions.DrivingRestrictionTable;
import restrictions.Restriction;
import restrictions.RestrictionForBenzine;
import restrictions.RestrictionForDiesel;

public class Inspection implements Runnable {
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
        while (!Thread.interrupted()) {
            double pollutionAmountAfterReset = 0;

            try {
                double pollutionWhenRestrictionApplies = pollutionDatabase.
                        getTotalPollutionWhenRestrictionApplies(restriction);
                if (pollutionDatabase.getInternalCombustionMotorCount() >= 70) {
                    pollutionAmountAfterReset = pollutionWhenRestrictionApplies * 0.4;
                }
                System.out.println("====== PollutionWhenRestrictionApplies: " + pollutionWhenRestrictionApplies
                        + " ======");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("........" + restriction.getPollutionRestriction() + ".BLOCK......................" +
                        ".........Waiting 5000 ms..............");
                Thread.sleep(10000);
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

            System.out.println("........" + restriction.getPollutionRestriction() + ".UNBLOCK........................" +
                    "..................UNBLOCKED..............");

        }
    }
}