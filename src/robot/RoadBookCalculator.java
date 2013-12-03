package robot;

import java.util.*;

import static robot.Direction.*;
import static robot.Instruction.*;
import static robot.MapTools.clockwise;
import static robot.MapTools.counterclockwise;
import static robot.MapTools.nextForwardPosition;

public class RoadBookCalculator {

    static RoadBook calculateRoadBook(Direction direction, Coordinates position, Coordinates destination, ArrayList<Instruction> instructions) {
        List<Direction> directionList = new ArrayList<Direction>();
        if (destination.getX() < position.getX()) directionList.add(WEST);
        if (destination.getX() > position.getX()) directionList.add(Direction.EAST);
        if (destination.getY() > position.getY()) directionList.add(Direction.SOUTH);
        if (destination.getY() < position.getY()) directionList.add(Direction.NORTH);
        if (directionList.isEmpty()) return new RoadBook(instructions);
        if (directionList.contains(direction)) {
            instructions.add(FORWARD);
            return calculateRoadBook(direction, nextForwardPosition(position, direction), destination, instructions);
        } else {
            instructions.add(TURNRIGHT);
            return calculateRoadBook(MapTools.clockwise(direction), position, destination, instructions);
        }
    }

    static RoadBook calculateRoadBook(LandSensor sensor, Direction direction, Coordinates position, Coordinates destination, ArrayList<Instruction> instructions) throws LandSensorDefaillance, UndefinedRoadbookException {
        if (position.equals(destination)) return new RoadBook(instructions);
        List<Direction> directionList = new ArrayList<Direction>();
        if (destination.getX() < position.getX()) directionList.add(WEST);
        if (destination.getX() > position.getX()) directionList.add(Direction.EAST);
        if (destination.getY() > position.getY()) directionList.add(Direction.SOUTH);
        if (destination.getY() < position.getY()) directionList.add(Direction.NORTH);
        List<Direction> directionsToExplored = new ArrayList<Direction>(Arrays.asList(NORTH, SOUTH, EAST, WEST));
        directionsToExplored.remove(Direction.oppositeDirection(direction));
        directionList.remove(Direction.oppositeDirection(direction));
        while (!directionsToExplored.isEmpty()) {
            if (directionList.contains(direction) && sensor.isAccessible(nextForwardPosition(position, direction))) {
                try {
                    directionsToExplored.remove(direction);
                    directionList.remove(direction);
                    //instructions.add(TAG);
                    instructions.add(FORWARD);
                    return calculateRoadBook(sensor, direction, nextForwardPosition(position, direction), destination, instructions);
                } catch (UndefinedRoadbookException e) {
                    int last = instructions.lastIndexOf(FORWARD);
                    for (int i = instructions.size() - 1; i >= last; i--) {
                        instructions.remove(i);
                    }
                }
            } else if (directionList.contains(direction)) { // !sensor.isAccessible(nextForwardPosition(position, direction)
                directionsToExplored.remove(direction);
                directionList.remove(direction);
                instructions.add(TURNRIGHT);
                direction = clockwise(direction);
            } else if (!directionList.isEmpty()) {
                instructions.add(TURNRIGHT);
                direction = clockwise(direction);
            } else if (directionList.isEmpty() && directionsToExplored.contains(direction) && sensor.isAccessible(nextForwardPosition(position, direction))) {
                try {
                    directionsToExplored.remove(direction);
                    instructions.add(FORWARD);
                    return calculateRoadBook(sensor, direction, nextForwardPosition(position, direction), destination, instructions);
                } catch (UndefinedRoadbookException e) {
                    int last = instructions.lastIndexOf(FORWARD);
                    for (int i = instructions.size() - 1; i >= last; i--) {
                        instructions.remove(i);
                    }
                }
            } else if (directionList.isEmpty() && directionsToExplored.contains(direction)) {
                directionsToExplored.remove(direction);
                instructions.add(TURNRIGHT);
                direction = clockwise(direction);
            } else if (directionList.isEmpty()) {
                instructions.add(TURNRIGHT);
                direction = clockwise(direction);
            }
        }
        throw new UndefinedRoadbookException();
    }


}