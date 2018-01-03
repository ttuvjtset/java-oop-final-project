package vertex;


public class Nadzor implements Runnable {
    private PollutionDB pollutionDB;
    private Block block;

    public Nadzor(PollutionDB pollutionDB, Block block) {

        this.pollutionDB = pollutionDB;
        this.block = block;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                double pollutionWhenBlockIssued = pollutionDB.blockConditionMet();
                System.out.println("pollutionWhenBlockIssued " + pollutionWhenBlockIssued);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("...................Waiting 5000 ms..............");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            block.releaseAllBlocks();
            System.out.println("block released");
            pollutionDB.informAboutUnblock();
            pollutionDB.resetPollutionCounter();
        }
    }
}
