package motors;


public class BenzineMotor implements Motor {
    @Override
    public double getPollutionRatio() {
        return 2;
    }

    @Override
    public String getMotorType() {
        return "Benzine";
    }
}
