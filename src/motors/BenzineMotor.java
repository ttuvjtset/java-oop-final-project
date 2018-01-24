package motors;


public class BenzineMotor implements Motor {

    private static final int POLLUTION_RATIO_FOR_BENZINE_MOTORS = 2;

    @Override
    public double getPollutionRatio() {
        return POLLUTION_RATIO_FOR_BENZINE_MOTORS;
    }

    @Override
    public String getMotorType() {
        return "Benzine";
    }
}
