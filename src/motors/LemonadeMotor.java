package motors;


public class LemonadeMotor implements Motor {
    @Override
    public double getPollutionRatio() {
        return 0.5;
    }

    @Override
    public String getMotorType() {
        return "Lemonade";
    }
}
