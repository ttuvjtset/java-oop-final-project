package motors;

public class DieselMotor implements Motor {

    private static final int POLLUTION_RATIO_FOR_DIESEL_MOTORS = 3;

    @Override
    public double getPollutionRatio() {
        return POLLUTION_RATIO_FOR_DIESEL_MOTORS;
    }

    @Override
    public String getMotorType() {
        return "Diesel";
    }
}
