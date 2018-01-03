package vertex;


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
            try {
                double pollutionWhenBlockIssued = pollutionDatabase.blockConditionMetForCars(restriction);
                System.out.println("pollutionWhenBlockIssued " + pollutionWhenBlockIssued);
                System.out.println(drivingRestrictions.isBlockedForDieselCars() + " " + drivingRestrictions.isBlockedForBenzineCars());
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
                drivingRestrictions.releaseDieselBlock();
            } else if (restriction instanceof RestrictionForBenzine) {
                drivingRestrictions.releaseBenzineBlock();
            }

            System.out.println("drivingRestrictions released");
            pollutionDatabase.resetPollutionCounter();
            pollutionDatabase.informAboutUnblock();
            System.out.println("tyt");
            System.out.println("........" + restriction.getPollutionRestriction() + ".UNBLOCK..........................................UNBLOCKED..............");

        }
    }
}
