package robot;

import org.junit.Assert;
import org.junit.Test;

import static robot.Direction.*;

/**
 * Created with IntelliJ IDEA.
 * User: fabrice
 * Date: 04/12/13
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class DirectionUnitTest {

    @Test
    public void testOppositeDirection() {
        Assert.assertEquals(SOUTH, oppositeDirection(NORTH));
        Assert.assertEquals(NORTH, oppositeDirection(SOUTH));
        Assert.assertEquals(WEST, oppositeDirection(EAST));
        Assert.assertEquals(EAST, oppositeDirection(WEST));

    }
}
