import inspection.PollutionDatabase;


public class Bird implements Runnable {

    private static final int BIRD_SINGING_PERIOD = 4000;
    private static final double POLLUTION_THRESHOLD = 400.0;

    private PollutionDatabaseView pollutionDatabaseView;
    private PollutionDatabase pollutionDatabase;

    Bird(PollutionDatabaseView pollutionDatabaseView, PollutionDatabase pollutionDatabase) {
        this.pollutionDatabaseView = pollutionDatabaseView;
        this.pollutionDatabase = pollutionDatabase;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(BIRD_SINGING_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (pollutionDatabase.getTotalPollutionAmount().doubleValue() < POLLUTION_THRESHOLD) {
                System.out.println("BIRD: Puhas õhk on puhas õhk on rõõmus linnu elu!");
            } else {
                System.out.println("BIRD: Inimene tark, inimene tark - saastet täis on linnapark");
            }

            System.out.println(pollutionDatabaseView.getJSON(pollutionDatabase));
        }
    }
}
