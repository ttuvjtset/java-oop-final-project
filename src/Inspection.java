import restrictions.Restriction;
import restrictions.RestrictionForBenzine;
import restrictions.RestrictionForDiesel;

public class Inspection implements Runnable {
    private PollutionDatabase pollutionDatabase;
    private DrivingRestrictions drivingRestrictions;
    private Restriction restriction;

    Inspection(PollutionDatabase pollutionDatabase, DrivingRestrictions drivingRestrictions, Restriction restriction) {
        this.pollutionDatabase = pollutionDatabase;
        this.drivingRestrictions = drivingRestrictions;
        this.restriction = restriction;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            double pollutionAmountAfterReset = 0;

            try {
                double pollutionWhenRestrictionApplies = pollutionDatabase.getTotalPollutionWhenRestrictionApplies(restriction);
                if (pollutionDatabase.getInternalCombustionMotorCount() >= 70) {
                    pollutionAmountAfterReset = pollutionWhenRestrictionApplies * 0.4;
                }
                System.out.println("====== PollutionWhenRestrictionApplies: " + pollutionWhenRestrictionApplies + " ======");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("........" + restriction.getPollutionRestriction() + ".BLOCK...............................Waiting 5000 ms..............");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (restriction instanceof RestrictionForDiesel) {
                drivingRestrictions.releaseBlockForDiesel();
            } else if (restriction instanceof RestrictionForBenzine) {
                drivingRestrictions.releaseBlockForBenzine();
            }

            System.out.println("====== Driving Restrictions Released ======");
            pollutionDatabase.resetPollutionCounter(pollutionAmountAfterReset);
            pollutionDatabase.informAboutReleasedRestrictions();

            System.out.println("........" + restriction.getPollutionRestriction() + ".UNBLOCK..........................................UNBLOCKED..............");

        }
    }
}
