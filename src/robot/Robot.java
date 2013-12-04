package robot;

import java.util.ArrayList;

import static robot.Direction.*;
import static robot.Instruction.*;

public class Robot {

    private Coordinates position;
    private Direction direction;
    private boolean isLanded;
    private RoadBook roadBook;

    public Robot() {
        isLanded = false;
    }

    public void land(Coordinates landPosition) {
        position = landPosition;
        direction = NORTH;
        isLanded = true;
    }

    public int getXposition() throws UnlandedRobotException {
        if (!isLanded) throw new UnlandedRobotException();
        return position.getX();
    }

    public int getYposition() throws UnlandedRobotException {
        if (!isLanded) throw new UnlandedRobotException();
        return position.getY();
    }

    public Direction getDirection() throws UnlandedRobotException {
        if (!isLanded) throw new UnlandedRobotException();
        return direction;
    }

    public void moveForward() throws UnlandedRobotException, InsufficientChargeException {
        if (!isLanded) throw new UnlandedRobotException();
        position = MapTools.nextForwardPosition(position, direction);
    }

    public void moveBackward() throws UnlandedRobotException, InsufficientChargeException {
        if (!isLanded) throw new UnlandedRobotException();
        position = MapTools.nextBackwardPosition(position, direction);
    }

    public void turnLeft() throws UnlandedRobotException {
        if (!isLanded) throw new UnlandedRobotException();
        direction = MapTools.counterclockwise(direction);
    }

    public void turnRight() throws UnlandedRobotException {
        if (!isLanded) throw new UnlandedRobotException();
        direction = MapTools.clockwise(direction);
    }


    public void setRoadBook(RoadBook roadBook) {
        this.roadBook = roadBook;
    }

    public void letsGo() throws UnlandedRobotException, UndefinedRoadbookException, InsufficientChargeException {
        if (roadBook == null) throw new UndefinedRoadbookException();
        while (roadBook.hasInstruction()) {
            Instruction nextInstruction = roadBook.next();
            if (nextInstruction == FORWARD) moveForward();
            else if (nextInstruction == BACKWARD) moveBackward();
            else if (nextInstruction == TURNLEFT) turnLeft();
            else if (nextInstruction == TURNRIGHT) turnRight();
        }
    }

    public void computeRoadTo(Coordinates destination) throws UnlandedRobotException {
        if (!isLanded) throw new UnlandedRobotException();
        setRoadBook(RoadBookCalculator.calculateRoadBook(direction, position, destination, new ArrayList<Instruction>()));
    }

}
