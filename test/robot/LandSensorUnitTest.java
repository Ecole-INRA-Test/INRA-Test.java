package robot;

import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Random;


public class LandSensorUnitTest {


    @Test
    public void testget() throws LandSensorDefaillance, InaccessibleCoordinate {
        Random random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt(5)).thenReturn(0, 3);
        LandSensor sensor = new LandSensor(random);
        Assert.assertEquals(2.0,sensor.getPointToPointEnergyCoefficient(new Coordinates(0,0), new Coordinates(1,0)));
    }

}
