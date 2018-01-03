package motors;


public class ElectricMotor implements Motor {
    @Override
    public double getPollutionRatio() {
        return 0.1;
    }

    @Override
    public String getMotorType() {
        return "Electric";
    }
}
