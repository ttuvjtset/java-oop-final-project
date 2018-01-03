package vertex;

/**
 * Created by root on 03.01.2018.
 */
public class Nadzor implements Runnable {
    private PollutionDB pollutionDB;

    public Nadzor(PollutionDB pollutionDB) {

        this.pollutionDB = pollutionDB;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                if (pollutionDB.getTotalPollutionAmount() > 200) {
                    System.out.println("HHHHHHH");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
