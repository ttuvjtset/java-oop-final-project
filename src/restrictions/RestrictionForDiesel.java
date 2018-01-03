package restrictions;


public class RestrictionForDiesel implements Restriction {
    @Override
    public int getPollutionRestriction() {
        return 400;
    }
}
