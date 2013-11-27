package robot;

/**
 * Created with IntelliJ IDEA.
 * User: fabrice
 * Date: 26/11/13
 * Time: 16:52
 * To change this template use File | Settings | File Templates.
 */
public class InaccessibleCoordinate extends Exception {
    private Coordinates coordinate;

    public InaccessibleCoordinate(Coordinates coordinate) {
        this.coordinate = coordinate;
    }
}
