import inspection.PollutionDatabase;
import motors.BenzineMotor;


public class PollutionDatabaseView {
    private long benzineCars;
    private long dieselCars;
    private long electricCars;
    private long lemonadeCars;

    String getJSON(PollutionDatabase pollutionDatabase) {
        updateDataFromDatabase(pollutionDatabase);

        double ecoMotorsPercentage = ((((double) electricCars + (double) lemonadeCars)
                / ((double) benzineCars + (double) dieselCars + (double) electricCars + (double) lemonadeCars)) * 100);

        return "{\n" +
                "benzineCars: " + benzineCars + "\n" +
                "dieselCars: " + dieselCars + "\n" +
                "electricCars: " + electricCars + "\n" +
                "lemonadeCars: " + lemonadeCars + "\n" +
                "ecoMotorsPercentage: " + ecoMotorsPercentage + "\n" +
                "}";
    }

    String getText(PollutionDatabase pollutionDatabase) {
        updateDataFromDatabase(pollutionDatabase);

        double ecoMotorsPercentage = ((((double) electricCars + (double) lemonadeCars)
                / ((double) benzineCars + (double) dieselCars + (double) electricCars + (double) lemonadeCars)) * 100);

        return "benzineCars: " + benzineCars + "\n" +
                "dieselCars: " + dieselCars + "\n" +
                "electricCars: " + electricCars + "\n" +
                "lemonadeCars: " + lemonadeCars + "\n" +
                "ecoMotorsPercentage: " + ecoMotorsPercentage;
    }

    private void updateDataFromDatabase(PollutionDatabase pollutionDatabase) {
        benzineCars = pollutionDatabase.filterMotorsByCondition(motor -> motor instanceof BenzineMotor).count();
        dieselCars = pollutionDatabase.filterMotorsByCondition(motor -> motor instanceof BenzineMotor).count();
        electricCars = pollutionDatabase.filterMotorsByCondition(motor -> motor instanceof BenzineMotor).count();
        lemonadeCars = pollutionDatabase.filterMotorsByCondition(motor -> motor instanceof BenzineMotor).count();
    }


}
