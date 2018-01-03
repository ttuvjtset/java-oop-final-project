package motors;

public class DieselMotor implements Motor {
    @Override
    public double getPollutionRatio() {
        return 3;
    }

    @Override
    public String getMotorType() {
        return "Diesel";
    }
}
