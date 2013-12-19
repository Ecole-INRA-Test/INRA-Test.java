package robot;

import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.mockito.Matchers.anyInt;


public class LandSensorUnitTest {


    @Test
    public void testget() throws LandSensorDefaillance, InaccessibleCoordinate {
        Random random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt()).thenReturn(1234);
        Mockito.when(random.nextInt(anyInt())).thenReturn(0, 3);
        LandSensor sensor = new LandSensor(random);
        Assert.assertEquals(2.0,sensor.getPointToPointEnergyCoefficient(new Coordinates(0,0), new Coordinates(1,0)));
    }

    @Test (expected = InaccessibleCoordinate.class)
    public void testGetEnergyToInfranchissable() throws LandSensorDefaillance, InaccessibleCoordinate {
        Random random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt()).thenReturn(1234, 10);
        Mockito.when(random.nextInt(anyInt())).thenReturn(0);
        LandSensor sensor = new LandSensor(random);
        sensor.getPointToPointEnergyCoefficient(new Coordinates(0,0), new Coordinates(1,0));
    }

    @Test (expected = LandSensorDefaillance.class)
    public void testGetEnergyWithDefaillance() throws LandSensorDefaillance, InaccessibleCoordinate {
        Random random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt()).thenReturn(1234);
        Mockito.when(random.nextInt(anyInt())).thenReturn(5, 0);
        LandSensor sensor = new LandSensor(random);
        sensor.getPointToPointEnergyCoefficient(new Coordinates(0,0), new Coordinates(1,0));
    }

    @Test
    public void testIsAccessible() throws LandSensorDefaillance {
        Random random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt()).thenReturn(1234);
        Mockito.when(random.nextInt(anyInt())).thenReturn(0, 1, 2, 3);
        LandSensor sensor = new LandSensor(random);
        sensor.cartographier(new Coordinates(0,0));
        Assert.assertTrue(sensor.isAccessible(new Coordinates(0,0)));
        Assert.assertTrue(sensor.isAccessible(new Coordinates(0,1)));
        Assert.assertTrue(sensor.isAccessible(new Coordinates(0,2)));
        Assert.assertTrue(sensor.isAccessible(new Coordinates(0,3)));
    }

    @Test
    public void testIsUnAccessible() throws LandSensorDefaillance {
        Random random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt()).thenReturn(0);
        LandSensor sensor = new LandSensor(random);
        Assert.assertFalse(sensor.isAccessible(new Coordinates(0,0)));
    }

    @Test
    public void testCartographie() throws LandSensorDefaillance {
        LandSensor landSensor = new LandSensor(new Random());
        landSensor.cartographier(new Coordinates(1,1));
        for (String ligne : landSensor.carte()) {
            System.out.println(ligne);
        }
        System.out.println("");
        landSensor.cartographier(new Coordinates(-2,-1));
        for (String ligne : landSensor.carte()) {
            System.out.println(ligne);
        }
        System.out.println("");
        landSensor.cartographier(new Coordinates(-6, 4));
        for (String ligne : landSensor.carte()) {
            System.out.println(ligne);
        }
    }

    @Test
    public void test() {
        for (int i = 10; i <1000 ; i++) {
            System.out.println(i + " : " + (char) i );

        }
    }
}
