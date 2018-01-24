package restrictions;


public class RestrictionForBenzine implements Restriction {

    private static final int POLLUTION_THRESHOLD_FOR_BENZINE_MOTORS = 500;

    @Override
    public int getPollutionRestriction() {
        return POLLUTION_THRESHOLD_FOR_BENZINE_MOTORS;
    }
}
