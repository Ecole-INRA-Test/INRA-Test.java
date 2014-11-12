package robot;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static robot.Instruction.*;

public class InstructionListToolUnitTest {

    @Test
    public void testCompacteId() {
        ArrayList<Instruction> instructions = new ArrayList<Instruction>(Arrays.asList(FORWARD, TURNRIGHT, FORWARD, TURNLEFT, BACKWARD, FORWARD, FORWARD, FORWARD));
        Assert.assertEquals(instructions, InstructionListTool.compacte(instructions));
    }
    @Test
    public void testCompacteThree() {
        ArrayList<Instruction> instructions = new ArrayList<Instruction>(Arrays.asList(FORWARD, TURNRIGHT,TURNRIGHT, TURNRIGHT, BACKWARD, FORWARD, FORWARD, FORWARD));
        ArrayList<Instruction> compacte = new ArrayList<Instruction>(Arrays.asList(FORWARD, TURNLEFT, BACKWARD, FORWARD, FORWARD, FORWARD));
        Assert.assertEquals(compacte, InstructionListTool.compacte(instructions));
    }
    @Test
    public void testCompacte() {
        ArrayList<Instruction> instructions = new ArrayList<Instruction>(Arrays.asList(FORWARD, TURNRIGHT,FORWARD, TURNRIGHT,TURNRIGHT, BACKWARD, FORWARD, FORWARD, FORWARD));
        Assert.assertEquals(instructions, InstructionListTool.compacte(instructions));
    }

    @Test
    public void testCompacteTurnRightSeul() {
        // Ce test constate que compacte contient une erreur (la liste, si elle ne comporte qu'une instructions TURNRIGHT, est vidée)
        ArrayList<Instruction> instructions = new ArrayList<Instruction>(Arrays.asList(TURNRIGHT));
        Assert.assertEquals(instructions, InstructionListTool.compacte(instructions));
    }

    @Test
    public void testCompacte3TurnRight() {
        ArrayList<Instruction> instructions = new ArrayList<Instruction>(Arrays.asList(TURNRIGHT, TURNRIGHT, TURNRIGHT));
        ArrayList<Instruction> compacte = new ArrayList<Instruction>(Arrays.asList(TURNLEFT));
        Assert.assertEquals(compacte, InstructionListTool.compacte(instructions));
    }


}
