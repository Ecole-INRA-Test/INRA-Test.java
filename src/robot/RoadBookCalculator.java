package robot;

import java.util.*;

import static robot.Direction.*;
import static robot.Instruction.*;
import static robot.InstructionListTool.concatene;
import static robot.MapTools.clockwise;
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

    static RoadBook calculateRoadBook(LandSensor sensor, Direction direction, Coordinates position, Coordinates destination, List<Instruction> instructions, List<Coordinates> trace) throws LandSensorDefaillance, UndefinedRoadbookException {
        if (position.equals(destination)) return new RoadBook(InstructionListTool.compacte(instructions));
        List<Direction> privilegeDirections = computePrivilegeDirection(position, destination);
        List<Direction> directionsToExplored = new ArrayList<Direction>(Arrays.asList(NORTH, SOUTH, EAST, WEST));
        directionsToExplored.remove(Direction.oppositeDirection(direction));
        privilegeDirections.remove(Direction.oppositeDirection(direction));
        while (!directionsToExplored.isEmpty()) {
            if (privilegeDirections.contains(direction) || privilegeDirections.isEmpty()) {
                privilegeDirections.remove(direction);
                if (directionsToExplored.contains(direction)) {
                    directionsToExplored.remove(direction);
                    Coordinates nextPos = nextForwardPosition(position, direction);
                    if (sensor.isAccessible(nextPos) && !trace.contains(nextPos)) {
                        try {
                            return calculateRoadBook(sensor, direction, nextPos, destination, concatene(instructions, FORWARD), concatene(trace, nextPos));
                        } catch (UndefinedRoadbookException e) {
                        }
                    }
                }
            }

            instructions.add(TURNRIGHT);
            direction = clockwise(direction);

        }

        throw new UndefinedRoadbookException();
    }

    private static List<Direction> computePrivilegeDirection(Coordinates position, Coordinates destination) {
        List<Direction> privilegeDirections = new ArrayList<Direction>();
        if (destination.getX() < position.getX()) privilegeDirections.add(WEST);
        if (destination.getX() > position.getX()) privilegeDirections.add(Direction.EAST);
        if (destination.getY() > position.getY()) privilegeDirections.add(Direction.SOUTH);
        if (destination.getY() < position.getY()) privilegeDirections.add(Direction.NORTH);
        return privilegeDirections;
    }


}