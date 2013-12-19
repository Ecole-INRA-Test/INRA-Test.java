package robot;

import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static robot.Direction.*;

public class RobotUnitTest {

    @Test
    public void testLand() throws UnlandedRobotException, LandSensorDefaillance {
        //---DEFINE---
        Robot robot = new Robot(1.0, new Battery());
        LandSensor sensor = Mockito.mock(LandSensor.class);
        //---WHEN---
        robot.land(new Coordinates(3, 0), sensor);
        //---THEN---
        Assert.assertEquals(NORTH, robot.getDirection());
        Assert.assertEquals(3, robot.getXposition());
        Assert.assertEquals(0, robot.getYposition());
    }

    // tester l'apparition d'une exception, l'annotation @Test intègre expected suivi de la classe de l'exception attendue
    // Attention : il est parfois nécessaire de s'assurer que l'exception n'apparaît pas avant la dernière instruction du test
    @Test(expected = UnlandedRobotException.class)
    public void testRobotMustBeLandedBeforeMoveForward() throws Exception {
        Robot robot = new Robot(1.0, new Battery());
        robot.moveForward();
    }

    @Test
    public void testMoveForward() throws Exception {
        //---DEFINE---
        // utilisation de mock pour la Battery et le LandSensor (test en isolation)
        Battery cells = mock(Battery.class);
        LandSensor sensor = mock(LandSensor.class);
        // quand on appelle la méthode getPointToPointEnergyCoefficient avec n'importe quel paramètre sur le mock on obtient en retour 1
        when(sensor.getPointToPointEnergyCoefficient(any(Coordinates.class), any(Coordinates.class))).thenReturn(1.0);
        when(cells.canDeliver(anyDouble())).thenReturn(true);
        doNothing().when(cells).use(anyDouble());
        Robot robot = new Robot(1.0, cells);
        robot.land(new Coordinates(3, 0), sensor);
        int currentXposition = robot.getXposition();
        int currentYposition = robot.getYposition();
        //---- WHEN ----
        robot.moveForward();
        //-----THEN ----
        Assert.assertEquals(currentXposition, robot.getXposition());
        Assert.assertEquals(currentYposition - 1, robot.getYposition());

    }

    @Test(expected = InsufficientChargeException.class)
    public void testMoveForwardWithInsufficientCharge() throws Exception {
        Battery cells = mock(Battery.class);
        Mockito.doThrow(new InsufficientChargeException()).doNothing().when(cells).use(anyDouble());
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.getPointToPointEnergyCoefficient(any(Coordinates.class), any(Coordinates.class))).thenReturn(5.0, 2.0, 5.0, 1.0);
        Robot robot = new Robot(2.0, cells);
        robot.land(new Coordinates(3, 0), sensor);
        robot.moveForward();
    }

    private void landNoEnergyConsumeRobot(Robot robot) throws Exception {
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.getPointToPointEnergyCoefficient(any(Coordinates.class), any(Coordinates.class))).thenReturn(1.0);
        robot.land(new Coordinates(3, 0), sensor);
    }

    @Test
    public void testMoveBackward() throws Exception {
        //---DEFINE---
        // utilisation de mock pour la Battery et le LandSensor (test en isolation)
        Battery cells = mock(Battery.class);
        LandSensor sensor = mock(LandSensor.class);
        // quand on appelle la méthode getPointToPointEnergyCoefficient avec n'importe quel paramètre sur le mock on obtient en retour 1
        when(sensor.getPointToPointEnergyCoefficient(any(Coordinates.class), any(Coordinates.class))).thenReturn(1.0);
        when(cells.canDeliver(anyDouble())).thenReturn(true);
        doNothing().when(cells).use(anyDouble());
        Robot robot = new Robot(1.0, cells);
        robot.land(new Coordinates(3, 0), sensor);
        int currentXposition = robot.getXposition();
        int currentYposition = robot.getYposition();
        //---WHEN---
        robot.moveBackward();
        //---THEN---
        Assert.assertEquals(currentXposition, robot.getXposition());
        Assert.assertEquals(currentYposition + 1, robot.getYposition());
    }

    @Test(expected = UnlandedRobotException.class)
    public void testRobotMustBeLandedBeforeTurnLeft() throws Exception {
        Robot robot = new Robot(1.0, new Battery());
        robot.turnLeft();
    }

    @Test(expected = InsufficientChargeException.class)
    public void testTurnLeftWithInsufficientEnergy() throws Exception {
        Battery cells = mock(Battery.class);
        LandSensor sensor = mock(LandSensor.class);
        when(cells.canDeliver(anyDouble())).thenReturn(false);
        Robot robot = new Robot(1.0, cells);
        robot.land(new Coordinates(3, 0), sensor);
        robot.turnLeft();
    }

    @Test
    public void testTurnLeft() throws Exception {
        Battery cells = mock(Battery.class);
        LandSensor sensor = mock(LandSensor.class);
        when(cells.canDeliver(anyDouble())).thenReturn(true);
        doNothing().when(cells).use(anyDouble());
        Robot robot = new Robot(1.0, cells);
        robot.land(new Coordinates(3, 0), sensor);
        robot.turnLeft();
        Assert.assertEquals(WEST, robot.getDirection());
        robot.turnLeft();
        Assert.assertEquals(SOUTH, robot.getDirection());
        robot.turnLeft();
        Assert.assertEquals(EAST, robot.getDirection());
        robot.turnLeft();
        Assert.assertEquals(NORTH, robot.getDirection());
    }

    @Test(expected = UnlandedRobotException.class)
    public void testRobotMustBeLandedBeforeTurnRight() throws Exception {
        Robot robot = new Robot(1.0, new Battery());
        robot.turnRight();
    }

    @Test
    public void testTurnRight() throws Exception {
        Battery cells = mock(Battery.class);
        when(cells.canDeliver(anyDouble())).thenReturn(true);
        doNothing().when(cells).use(anyDouble());
        Robot robot = new Robot(1.0, cells);
        LandSensor sensor = mock(LandSensor.class);
        robot.land(new Coordinates(3, 0), sensor);
        robot.turnRight();
        Assert.assertEquals(EAST, robot.getDirection());
        robot.turnRight();
        Assert.assertEquals(SOUTH, robot.getDirection());
        robot.turnRight();
        Assert.assertEquals(WEST, robot.getDirection());
        robot.turnRight();
        Assert.assertEquals(NORTH, robot.getDirection());
    }

    @Test(expected = UndefinedRoadbookException.class)
    public void testLetsGoWithoutRoadbook() throws Exception {
        Robot robot = new Robot(1.0, mock(Battery.class));
        robot.land(new Coordinates(3, 0), mock(LandSensor.class));
        robot.letsGo();
    }

    @Test
    public void testLetsGo() throws Exception {
        Battery cells = mock(Battery.class);
        when(cells.canDeliver(anyDouble())).thenReturn(true);
        doNothing().when(cells).use(anyDouble());
        Robot robot = new Robot(1.0, cells);
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.getPointToPointEnergyCoefficient(any(Coordinates.class), any(Coordinates.class))).thenReturn(1.0);
        robot.land(new Coordinates(5, 7), sensor);
        robot.setRoadBook(new RoadBook(Arrays.asList(Instruction.FORWARD, Instruction.FORWARD, Instruction.TURNLEFT, Instruction.FORWARD)));
        robot.letsGo();
        Assert.assertEquals(4, robot.getXposition());
        Assert.assertEquals(5, robot.getYposition());
    }

    @Test(expected = UnlandedRobotException.class)
    public void testComputeRoadToWithUnlandedRobot() throws Exception {
        Robot robot = new Robot(1.0, null);
        robot.computeRoadTo(new Coordinates(3, 5));
    }

    @Test
    public void testComputeRoadTo() throws Exception {
        //TODO
        Robot robot = new Robot(1.0, new Battery());
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.getPointToPointEnergyCoefficient(any(Coordinates.class), any(Coordinates.class))).thenReturn(1.0);
        robot.land(new Coordinates(3, 0), sensor);
        robot.computeRoadTo(new Coordinates(0, -3));
        robot.letsGo();
        Assert.assertEquals(0, robot.getXposition());
        Assert.assertEquals(-6, robot.getYposition());
    }
}
