package vertex;


public class Nadzor implements Runnable {
    private PollutionDB pollutionDB;
    private volatile Block block;

    public Nadzor(PollutionDB pollutionDB, Block block) {

        this.pollutionDB = pollutionDB;
        this.block = block;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                double pollutionWhenBlockIssued = pollutionDB.blockConditionMetForBenzineCars();
                System.out.println("pollutionWhenBlockIssued " + pollutionWhenBlockIssued);
                System.out.println(block.isBlockedForDieselCars() + " " + block.isBlockedForBenzineCars());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("...................Waiting 5000 ms..............");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            block.releaseAllBlocks();
            System.out.println("block released");
            pollutionDB.resetPollutionCounter();
            pollutionDB.informAboutUnblock();
            System.out.println("tyt");

        }
    }
}
