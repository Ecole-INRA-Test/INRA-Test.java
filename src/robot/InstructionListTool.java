package robot;

import java.util.ArrayList;
import java.util.List;

import static robot.Instruction.TURNLEFT;
import static robot.Instruction.TURNRIGHT;

public class InstructionListTool {
     static List<Instruction> compacte(ArrayList<Instruction> instructions) {
        int cpt = 0;
        List<Instruction> copieCompacte = new ArrayList<Instruction>();
        for (int i = 0; i < instructions.size(); i++) {
            if (instructions.get(i) != TURNRIGHT) {
                if (cpt != 0) {
                    if (cpt == 3)
                        copieCompacte.add(TURNLEFT);
                    else {
                        while (cpt > 0) {
                            copieCompacte.add(TURNRIGHT);
                            cpt--;
                        }
                    }
                    cpt = 0;
                }
                copieCompacte.add(instructions.get(i));

            } else {
                cpt++;
            }
        }
        return copieCompacte;
    }
}