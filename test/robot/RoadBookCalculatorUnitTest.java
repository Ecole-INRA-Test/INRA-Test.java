package robot;

import org.junit.*;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static robot.Direction.*;
import static robot.Instruction.FORWARD;
import static robot.Instruction.TURNLEFT;
import static robot.Instruction.TURNRIGHT;
import static robot.RoadBookCalculator.calculateRoadBook;

public class RoadBookCalculatorUnitTest {

    RoadBook book;
    Coordinates startPosition;
    ArrayList<Instruction> instructions;

    @Before
    public void setUp() {
        book = null;
        startPosition = new Coordinates(1, 1);
        instructions = new ArrayList<Instruction>();
    }

    @After
    public void tearDown() {
        instructions.clear();
        book = null;
    }


    @Test
    public void testCalculateAtDestination() {
        book = calculateRoadBook(NORTH, startPosition, startPosition, instructions);
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateOneInstructionNorthRoad() {
        book = calculateRoadBook(NORTH, startPosition, new Coordinates(1, 0), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateOneInstructionSouthRoad() {
        book = calculateRoadBook(SOUTH, startPosition, new Coordinates(1, 2), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateOneInstructionEastRoad() {
        book = calculateRoadBook(EAST, startPosition, new Coordinates(2, 1), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateOneInstructionWestRoad() {
        book = calculateRoadBook(WEST, startPosition, new Coordinates(0, 1), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateStraightForwardRoad() {
        book = calculateRoadBook(WEST, startPosition, new Coordinates(-4, 1), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateNEroad() {
        book = calculateRoadBook(NORTH, startPosition, new Coordinates(2, 0), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateNWroad() {
        book = calculateRoadBook(NORTH, startPosition, new Coordinates(0, 0), instructions);
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateSEroad() {
        book = calculateRoadBook(NORTH, startPosition, new Coordinates(2, 2), instructions);
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testCalculateSWroad() {
        book = calculateRoadBook(NORTH, startPosition, new Coordinates(0, 2), instructions);
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertEquals(TURNRIGHT, book.next());
        Assert.assertEquals(Instruction.FORWARD, book.next());
        Assert.assertFalse(book.hasInstruction());
    }

    @Test
    public void testRouteSimple1() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(1, 0))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(3, 1))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -3))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -3))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(5, -2))).thenReturn(false);
        book = calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(5, -5), instructions, Collections.EMPTY_LIST);
        List<Instruction> expected = new ArrayList<Instruction>(Arrays.asList(TURNRIGHT, FORWARD, TURNLEFT, FORWARD, FORWARD, FORWARD, TURNRIGHT, FORWARD, FORWARD, TURNLEFT, FORWARD, FORWARD, FORWARD, TURNRIGHT, FORWARD));
        while (book.hasInstruction()) {
            Assert.assertEquals(expected.remove(0), book.next());
        }
        Assert.assertTrue(expected.isEmpty());
    }

    @Test
    public void testRouteSimple2() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(1, 0))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(5, -2))).thenReturn(false);
        book = calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(5, -5), instructions, Collections.EMPTY_LIST);
        List<Instruction> expected = new ArrayList<Instruction>(Arrays.asList(TURNRIGHT, FORWARD, FORWARD, FORWARD, FORWARD, TURNLEFT, FORWARD, FORWARD, TURNRIGHT, FORWARD, TURNLEFT, FORWARD, FORWARD, FORWARD, FORWARD, TURNLEFT, FORWARD));
        while (book.hasInstruction()) {
            Assert.assertEquals(expected.remove(0), book.next());
        }
        Assert.assertTrue(expected.isEmpty());
    }

    @Test
    public void testRouteCulDeSac() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(1, -6))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0, -5))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -5))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0, -4))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -4))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0, -3))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -3))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0, -2))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -2))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0, -1))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -1))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(0, 0))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, 0))).thenReturn(false);
        book = calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(5, -5), instructions, Collections.EMPTY_LIST);
        List<Instruction> expected = new ArrayList<Instruction>(Arrays.asList(TURNRIGHT, FORWARD, FORWARD, FORWARD, FORWARD, TURNLEFT, FORWARD, FORWARD, FORWARD, FORWARD, FORWARD, FORWARD));
        while (book.hasInstruction()) {
            Assert.assertEquals(expected.remove(0), book.next());
        }
        Assert.assertTrue(expected.isEmpty());
    }

    @Test(expected = UndefinedRoadbookException.class)
    public void testArriveeInaccessible() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        cartographie(sensor, startPosition, 2);
        when(sensor.isAccessible(new Coordinates(0, 0))).thenReturn(false);
        calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(0, 0), instructions, Collections.EMPTY_LIST);
    }

    @Test(expected = UndefinedRoadbookException.class)
    public void testArriveeHorsCarte() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        cartographie(sensor, startPosition, 3);
        book = calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(10, 10), instructions, Collections.EMPTY_LIST);
    }

    @Test(expected = UndefinedRoadbookException.class)
    public void testArriveeIsolee() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        cartographie(sensor, startPosition, 4);
        when(sensor.isAccessible(new Coordinates(3, -1))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(2, -2))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(4, -2))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(3, -3))).thenReturn(false);
        book = calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(3, -2), instructions, Collections.EMPTY_LIST);
        while (book.hasInstruction()) {
            System.out.println(book.next().toString());
        }
    }

    @Test(expected = UndefinedRoadbookException.class)
    public void testArriveeHorsCarteSurCarteReduite() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(false);
        closeCarte(sensor, startPosition, 4);
        when(sensor.isAccessible(new Coordinates(1, 0))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(1, -1))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(1, -2))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(2, -2))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(3, -2))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(3, -1))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(3, 0))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(2, 0))).thenReturn(true);
        calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(5, -5), instructions, Collections.EMPTY_LIST);
    }


    private void cartographie(LandSensor sensor, Coordinates startPosition, int distance) {
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(false);
        for (int i = startPosition.getX() - distance; i < startPosition.getX() + distance + 1; i++) {
            for (int j = startPosition.getY() - distance; j < startPosition.getY() + distance + 1; j++) {
                when(sensor.isAccessible(new Coordinates(i, j))).thenReturn(false);
            }
        }

    }


    private void closeCarte(LandSensor sensor, Coordinates startPosition, int distance) throws LandSensorDefaillance {
        for (int i = startPosition.getX() - distance; i < startPosition.getX() + distance + 1; i++) {
            when(sensor.isAccessible(new Coordinates(i, startPosition.getY() - distance))).thenReturn(false);
            when(sensor.isAccessible(new Coordinates(i, startPosition.getY() + distance))).thenReturn(false);
        }
        for (int i = startPosition.getY() - (distance - 1); i < startPosition.getY() + distance; i++) {
            when(sensor.isAccessible(new Coordinates(startPosition.getX() - distance, i))).thenReturn(false);
            when(sensor.isAccessible(new Coordinates(startPosition.getX() + distance, i))).thenReturn(false);
        }
    }

    @Test(expected = LandSensorDefaillance.class)
    public void testPanneDeSensor() throws LandSensorDefaillance, UndefinedRoadbookException {
        LandSensor sensor = mock(LandSensor.class);
        when(sensor.isAccessible(any(Coordinates.class))).thenReturn(true);
        when(sensor.isAccessible(new Coordinates(5, -4))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(4, -4))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(6, -4))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(4, -5))).thenThrow(LandSensorDefaillance.class);
        when(sensor.isAccessible(new Coordinates(6, -5))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(4, -6))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(5, -6))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(6, -6))).thenReturn(false);
        when(sensor.isAccessible(new Coordinates(5, -5))).thenReturn(false);
        book = calculateRoadBook(sensor, NORTH, startPosition, new Coordinates(5, -5), instructions, Collections.EMPTY_LIST);

    }




}
