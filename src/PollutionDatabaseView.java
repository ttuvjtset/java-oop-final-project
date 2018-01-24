import inspection.PollutionDatabase;
import motors.BenzineMotor;
import motors.DieselMotor;
import motors.ElectricMotor;
import motors.LemonadeMotor;


public class PollutionDatabaseView {
    private long benzineCars;
    private long dieselCars;
    private long electricCars;
    private long lemonadeCars;

    public String getJSON(PollutionDatabase pollutionDatabase) {
        updateDataFromDatabase(pollutionDatabase);

        double ecoMotorsPercentage = (((double) electricCars + (double) lemonadeCars)
                / ((double) benzineCars + (double) dieselCars + (double) electricCars + (double) lemonadeCars)) * 100;

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
                / ((double) benzineCars + (double) dieselCars + (double) electricCars + (double) lemonadeCars)) * 100);

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
