import inspection.PollutionDatabase;
import motors.BenzineMotor;
import motors.DieselMotor;
import motors.ElectricMotor;
import motors.LemonadeMotor;


public class PollutionDatabaseView {
    private static final int CONVERT_TO_PERCENTAGE = 100;
    private long benzineCars;
    private long dieselCars;
    private long electricCars;
    private long lemonadeCars;

    String getJSON(PollutionDatabase pollutionDatabase) {
        updateDataFromDatabase(pollutionDatabase);

        double ecoMotorsPercentage = (((double) electricCars + (double) lemonadeCars)
                / ((double) benzineCars + (double) dieselCars + (double) electricCars + (double) lemonadeCars)) *
                CONVERT_TO_PERCENTAGE;

        return "{\n" +
                "     benzineCars: " + benzineCars + "\n" +
                "     dieselCars: " + dieselCars + "\n" +
                "     electricCars: " + electricCars + "\n" +
                "     lemonadeCars: " + lemonadeCars + "\n" +
                "     ecoMotorsPercentage: " + ecoMotorsPercentage + "\n" +
                "}";
    }

    public String getText(PollutionDatabase pollutionDatabase) {
        updateDataFromDatabase(pollutionDatabase);

        double ecoMotorsPercentage = ((((double) electricCars + (double) lemonadeCars)
                / ((double) benzineCars + (double) dieselCars + (double) electricCars + (double) lemonadeCars)) *
                CONVERT_TO_PERCENTAGE);

        return "benzineCars: " + benzineCars + "\n" +
                "dieselCars: " + dieselCars + "\n" +
                "electricCars: " + electricCars + "\n" +
                "lemonadeCars: " + lemonadeCars + "\n" +
                "ecoMotorsPercentage: " + ecoMotorsPercentage;
    }

    private void updateDataFromDatabase(PollutionDatabase pollutionDatabase) {
        benzineCars = pollutionDatabase.filterMotorsByCondition(motor -> motor instanceof BenzineMotor).count();
        dieselCars = pollutionDatabase.filterMotorsByCondition(motor -> motor instanceof DieselMotor).count();
        electricCars = pollutionDatabase.filterMotorsByCondition(motor -> motor instanceof ElectricMotor).count();
        lemonadeCars = pollutionDatabase.filterMotorsByCondition(motor -> motor instanceof LemonadeMotor).count();
    }

}
