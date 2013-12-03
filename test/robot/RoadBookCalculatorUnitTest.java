package robot;

import apple.laf.JRSUIConstants;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static robot.Direction.*;
import static robot.RoadBookCalculator.calculateRoadBook;

public class RoadBookCalculatorUnitTest {

    RoadBook book;
    Coordinates startPosition;
    ArrayList<Instruction> instructions;

    @Before
    public void setUp(){
        book = null;
        startPosition = new Coordinates(1,1);
        instructions = new ArrayList<Instruction>();
    }


    @Test
    public void testCalculateAtDestination() {
        book = calculateRoadBook(NORTH, startPosition, startPosition, instructions);
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateOneInstructionNorthRoad() {
        book = calculateRoadBook(NORTH, startPosition, new Coordinates(1,0), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateOneInstructionSouthRoad() {
        book = calculateRoadBook(SOUTH, startPosition, new Coordinates(1,2), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateOneInstructionEastRoad() {
        book = calculateRoadBook(EAST, startPosition, new Coordinates(2,1), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateOneInstructionWestRoad() {
        book = calculateRoadBook(WEST, startPosition, new Coordinates(0,1), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateStraightForwardRoad() {
        book = calculateRoadBook(WEST, startPosition, new Coordinates(-4,1), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateNEroad() {
        book = calculateRoadBook(NORTH, startPosition, new Coordinates(2,0), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(Instruction.TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateNWroad() {
        book = calculateRoadBook(NORTH, startPosition, new Coordinates(0,0), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(Instruction.TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateSEroad() {
        book = calculateRoadBook(NORTH, startPosition, new Coordinates(2,2), instructions);
        Assert.assertEquals(Instruction.TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(Instruction.TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateSWroad() {
        book = calculateRoadBook(NORTH, startPosition, new Coordinates(0,2), instructions);
        Assert.assertEquals(Instruction.TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(Instruction.TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void test() throws LandSensorDefaillance, InaccessibleCoordinate, UndefinedRoadbookException {
        LandSensor sensor = Mockito.mock(LandSensor.class);
 //       when(sensor.getPointToPointEnergyCoefficient(startPosition, new Coordinates(1, 2))).thenThrow(new InaccessibleCoordinate(new Coordinates(1,2)));
        when(sensor.getPointToPointEnergyCoefficient(any(Coordinates.class), any(Coordinates.class))).thenReturn(1.0);
        book = calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(5,5), instructions);
    }

    @Test
    public void testRouteSimple1() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(1,0))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(3, 1))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2,-3))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2,-3))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(5,-2))).thenReturn(false);
        book = calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(5,-5), instructions);
        while (book.hasInstruction()) {
            System.out.println(book.next().toString());
        }
    }
    @Test
    public void testRouteSimple2() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(1,0))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(5,-2))).thenReturn(false);
        book = calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(5,-5), instructions);
        while (book.hasInstruction()) {
            System.out.println(book.next().toString());
        }
    }
    @Test
    public void testRouteCulDeSac() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(1,-6))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0,-5))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2,-5))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0,-4))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2,-4))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0,-3))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2,-3))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0,-2))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2,-2))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0,-1))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2,-1))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0,0))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2,0))).thenReturn(false);
        book = calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(5,-5), instructions);
        while (book.hasInstruction()) {
            System.out.println(book.next().toString());
        }
    }

}
