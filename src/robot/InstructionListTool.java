package robot;

import java.util.ArrayList;
import java.util.List;

import static robot.Instruction.TURNLEFT;
import static robot.Instruction.TURNRIGHT;

public class InstructionListTool {
    static List<Instruction> compacte(List<Instruction> instructions) {
        int cpt = 0;
        List<Instruction> copieCompacte = new ArrayList<Instruction>();
        for (int i = 0; i < instructions.size(); i++) {
            if (instructions.get(i) != TURNRIGHT) {
                if (cpt != 0) {
                    replaceListOfTurnRight(cpt, copieCompacte);
                    cpt = 0;
                }
                copieCompacte.add(instructions.get(i));

            } else {
                cpt++;
            }
        }
        replaceListOfTurnRight(cpt, copieCompacte);
        return copieCompacte;
    }

    private static void replaceListOfTurnRight(int cpt, List<Instruction> copieCompacte) {
        if (cpt == 3)
            copieCompacte.add(TURNLEFT);
        else {
            while (cpt > 0) {
                copieCompacte.add(TURNRIGHT);
                cpt--;
            }
        }
    }

    static <T> List<T> concatene(List<T> trace, T coordinates) {
        ArrayList<T> coordonnees = new ArrayList<T>(trace);
        coordonnees.add(coordinates);
        return coordonnees;
    }
}