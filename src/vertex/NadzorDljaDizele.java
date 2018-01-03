package vertex;


public class NadzorDljaDizele implements Runnable {
    private PollutionDB pollutionDB;
    private volatile Block block;

    public NadzorDljaDizele(PollutionDB pollutionDB, Block block) {

        this.pollutionDB = pollutionDB;
        this.block = block;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                double pollutionWhenBlockIssued = pollutionDB.blockConditionMetForDieselCars();
                System.out.println("pollutionWhenBlockIssued " + pollutionWhenBlockIssued);
                System.out.println(block.isBlockedForDieselCars() + " " + block.isBlockedForBenzineCars());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("........DIESEL.BLOCK...............................Waiting 5000 ms..............");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            block.releaseDieselBlock();
            System.out.println("block released");
            pollutionDB.resetPollutionCounter();
            pollutionDB.informAboutUnblock();
            System.out.println("tyt");
            System.out.println("........DIESEL.UNBLOCK..........................................UNBLOCKED..............");

        }
    }
}
