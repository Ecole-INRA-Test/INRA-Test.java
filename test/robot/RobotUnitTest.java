package robot;

import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static robot.Direction.EAST;
import static robot.Direction.WEST;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class RobotUnitTest {

    @Test
    public void testLand() throws UnlandedRobotException {
        //---DEFINE---
        Robot robot = new Robot();
        //---WHEN---
        robot.land(new Coordinates(3,0), null);
        //---THEN---
        Assert.assertEquals(3, robot.getXposition());
        Assert.assertEquals(0, robot.getYposition());
    }

    // tester l'apparition d'une exception, l'annotation @Test intègre expected suivi de la classe de l'exception attendue
    // Attention : il est parfois nécessaire de s'assurer que l'exception n'apparaît pas avant la dernière instruction du test
    @Test (expected = UnlandedRobotException.class)
    public void testRobotMustBeLandedBeforeAnyMove() throws Exception {
        Robot robot = new Robot();
        robot.moveForward();
    }

    @Test
    public void testMoveForward() throws Exception {
        Robot robot = new Robot();
        landNoEnergyConsumeRobot(robot);
        int currentXposition = robot.getXposition();
        int currentYposition = robot.getYposition();
        robot.moveForward();
        Assert.assertEquals(currentXposition, robot.getXposition());
        Assert.assertEquals(currentYposition-1, robot.getYposition());

    }

    private void landNoEnergyConsumeRobot(Robot robot) throws Exception {
        // utilisation d'un mock pour le LandSensor
        LandSensor sensor = Mockito.mock(LandSensor.class);
        // quand on appelle la méthode getPointToPointEnergyCoefficient avec n'importe quel paramètre sur le mock
        // on obtient en retour 0
        when(sensor.getPointToPointEnergyCoefficient(any(Coordinates.class), any(Coordinates.class))).thenReturn(0.0);
        // l'objet mock est considéré comme un véritable LandSensor par le robot et invoquera les méthodes sur l'objet
        robot.land(new Coordinates(3,0), sensor);
    }

    @Test
    public void testMoveForwardWithEnergyConsumptionAndSufficientCharge() throws Exception {
        //---DEFINE---
        Battery cells = Mockito.mock(Battery.class);
        Mockito.doNothing().when(cells).use(anyDouble());
        Mockito.when(cells.canDeliver(anyDouble())).thenReturn(true);
        Robot robot = new Robot(2.0, cells);
        LandSensor sensor = Mockito.mock(LandSensor.class);
        when(sensor.getPointToPointEnergyCoefficient(any(Coordinates.class), any(Coordinates.class))).thenReturn(1.0,2.0,5.0,1.0);
        robot.land(new Coordinates(3,0), sensor);

        //---WHEN---
        robot.moveForward();

        //---THEN---
        Assert.assertEquals(3, robot.getXposition());
        Assert.assertEquals(-1, robot.getYposition());
    }

    @Test (expected = InsufficientChargeException.class)
    public void testMoveForwardWithEnergyConsumptionAndInsufficientCharge() throws Exception {
        Battery cells = Mockito.mock(Battery.class);
        Mockito.doThrow(new InsufficientChargeException()).doNothing().when(cells).use(anyDouble());
        when(cells.timeToSufficientCharge(anyDouble())).thenReturn(0l);
        Robot robot = new Robot(2.0, cells);
        LandSensor sensor = Mockito.mock(LandSensor.class);
        when(sensor.getPointToPointEnergyCoefficient(any(Coordinates.class), any(Coordinates.class))).thenReturn(5.0,2.0,5.0,1.0);
        robot.land(new Coordinates(3,0), sensor);
        robot.moveForward();
    }

    @Test
    public void testMoveBackward() throws Exception {
        Robot robot = new Robot();
        landNoEnergyConsumeRobot(robot);
        int currentXposition = robot.getXposition();
        int currentYposition = robot.getYposition();
        robot.moveBackward();
        Assert.assertEquals(currentXposition, robot.getXposition());
        Assert.assertEquals(currentYposition+1, robot.getYposition());
    }

    @Test
    public void testTurnLeft() throws Exception {
        Robot robot = new Robot();
        landNoEnergyConsumeRobot(robot);
        robot.turnLeft();
        Assert.assertEquals(WEST, robot.getDirection());
    }

    @Test
    public void testTurnRight() throws Exception {
        Robot robot = new Robot();
        landNoEnergyConsumeRobot(robot);
        robot.turnRight();
        Assert.assertEquals(EAST, robot.getDirection());
    }

    @Test (expected = UndefinedRoadbookException.class)
    public void testLetsGoWithoutRoadbook() throws Exception {
        Robot robot = new Robot();
        landNoEnergyConsumeRobot(robot);
        robot.letsGo();
    }

    @Test
    public void testFollowInstruction() throws Exception {
        Robot robot = new Robot();
        LandSensor sensor = Mockito.mock(LandSensor.class);
        when(sensor.getPointToPointEnergyCoefficient(any(Coordinates.class), any(Coordinates.class))).thenReturn(1.0);
        robot.land(new Coordinates(5, 7), sensor);
        robot.setRoadBook(new RoadBook(Arrays.asList(Instruction.FORWARD, Instruction.FORWARD, Instruction.TURNLEFT, Instruction.FORWARD)));
        robot.letsGo();
        Assert.assertEquals(4, robot.getXposition());
        Assert.assertEquals(5, robot.getYposition());
    }

    @Test (expected = UnlandedRobotException.class)
    public void testMoveToWithUnlandedRobot() throws Exception {
        Robot robot = new Robot();
        robot.computeRoadTo(new Coordinates(3, 5));
    }

    @Test
    public void testMoveTo() throws UnlandedRobotException {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0), null);
        robot.computeRoadTo(new Coordinates(7, 5));
    }

    @Test
    public void testComputeRoadTo() throws Exception {
        Robot robot = new Robot();
        landNoEnergyConsumeRobot(robot);
        robot.computeRoadTo(new Coordinates(0, -6));
        robot.letsGo();
        Assert.assertEquals(0, robot.getXposition());
        Assert.assertEquals(-6, robot.getYposition());
    }
}
