package motors;


public class ElectricMotor implements Motor {

    private static final double POLLUTION_RATIO_FOR_ELECTRIC_MOTORS = 0.1;

    @Override
    public double getPollutionRatio() {
        return POLLUTION_RATIO_FOR_ELECTRIC_MOTORS;
    }

    @Override
    public String getMotorType() {
        return "Electric";
    }
}
