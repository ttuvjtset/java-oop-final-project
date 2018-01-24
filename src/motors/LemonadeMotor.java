package motors;


public class LemonadeMotor implements Motor {

    private static final double POLLUTION_RATIO_FOR_LEMONADE_MOTORS = 0.5;

    @Override
    public double getPollutionRatio() {
        return POLLUTION_RATIO_FOR_LEMONADE_MOTORS;
    }

    @Override
    public String getMotorType() {
        return "Lemonade";
    }
}
