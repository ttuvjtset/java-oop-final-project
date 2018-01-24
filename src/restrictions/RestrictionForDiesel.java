package restrictions;


public class RestrictionForDiesel implements Restriction {

    private static final int POLLUTION_THRESHOLD_FOR_DIESEL_MOTORS = 400;

    @Override
    public int getPollutionRestriction() {
        return POLLUTION_THRESHOLD_FOR_DIESEL_MOTORS;
    }
}
