import inspection.PollutionDatabase;


public class Bird implements Runnable {


    private PollutionDatabase pollutionDatabase;

    public Bird(PollutionDatabase pollutionDatabase) {

        this.pollutionDatabase = pollutionDatabase;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (pollutionDatabase.getTotalPollutionAmount().doubleValue() < 400.0) {
                System.out.println("BIRD: Puhas õhk on puhas õhk on rõõmus linnu elu!");
            } else {
                System.out.println("BIRD: Inimene tark, inimene tark - saastet täis on linnapark");
            }
        }
    }
}
