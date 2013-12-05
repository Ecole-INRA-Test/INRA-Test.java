package robot;

import com.sun.tools.javac.util.Pair;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: fabrice
 * Date: 03/12/13
 * Time: 22:41
 * To change this template use File | Settings | File Templates.
 */
public class Explorer {
    public static void main(String[] args) {
        System.out.println("Consommation de base du robot d'exploration ");
        Scanner scanner = new Scanner(System.in);
        double energy = scanner.nextDouble();
        Robot robot = new Robot(energy, new Battery());
        while (true) {
            displayMenu();
            String commande;
            do {
                commande = scanner.nextLine();
            } while (commande.length() != 1);
            switch (commande.charAt(0)) {
                case 'A':
                    System.out.println("coordonnées x,y de dépose du robot");
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();
                    robot.land(new Coordinates(x, y), new LandSensor(new Random(10)));
                    try {
                        System.out.println("Position actuelle : (" + robot.getXposition() + " ," + robot.getYposition() + ") - orientation : " + robot.getDirection());
                    } catch (UnlandedRobotException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    break;
                case 'Z':
                    try {
                        robot.moveForward();
                        System.out.println("Position actuelle : (" + robot.getXposition() + " ," + robot.getYposition() + ") - orientation : " + robot.getDirection());
                    } catch (UnlandedRobotException e) {
                        System.out.println("Le robot est encore en l'air, il doit se poser d'abord");
                    } catch (InsufficientChargeException e) {
                        System.out.println("Oups, piles vides... soyez patient, le soleil fait son oeuvre");
                    } catch (LandSensorDefaillance landSensorDefaillance) {
                        System.out.println("Aie, le module de détection du terrain est défaillant. Abandon de l'exploration");
                        throw new RuntimeException(landSensorDefaillance);
                    } catch (InaccessibleCoordinate inaccessibleCoordinate) {
                        System.out.println("le terrain devant le robot n'est pas praticable");
                    }
                    break;
                case 'S':
                    try {
                        robot.moveBackward();
                        System.out.println("Position actuelle : (" + robot.getXposition() + " ," + robot.getYposition() + ") - orientation : " + robot.getDirection());
                    } catch (UnlandedRobotException e) {
                        System.out.println("Le robot est encore en l'air, il doit se poser d'abord");
                    } catch (InsufficientChargeException e) {
                        System.out.println("Oups, piles vides... soyez patient, le soleil fait son oeuvre");
                    } catch (LandSensorDefaillance landSensorDefaillance) {
                        System.out.println("Aie, le module de détection du terrain est défaillant. Abandon de l'exploration");
                        throw new RuntimeException(landSensorDefaillance);
                    } catch (InaccessibleCoordinate inaccessibleCoordinate) {
                        System.out.println("le terrain derrière le robot n'est pas praticable");
                    }
                    break;
                case 'Q':
                    try {
                        robot.turnLeft();
                        System.out.println("Position actuelle : (" + robot.getXposition() + " ," + robot.getYposition() + ") - orientation : " + robot.getDirection());
                    } catch (UnlandedRobotException e) {
                        System.out.println("Le robot est encore en l'air, il doit se poser d'abord");
                    } catch (InsufficientChargeException e) {
                        System.out.println("Oups, piles vides... soyez patient, le soleil fait son oeuvre");
                    }
                    break;
                case 'D':
                    try {
                        robot.turnRight();
                        System.out.println("Position actuelle : (" + robot.getXposition() + " ," + robot.getYposition() + ") - orientation : " + robot.getDirection());
                    } catch (UnlandedRobotException e) {
                        System.out.println("Le robot est encore en l'air, il doit se poser d'abord");
                    } catch (InsufficientChargeException e) {
                        System.out.println("Oups, piles vides... soyez patient, le soleil fait son oeuvre");
                    }
                    break;
                case 'M':
                    System.out.println("coordonnées x,y de la destination");
                    int destx = scanner.nextInt();
                    int desty = scanner.nextInt();
                    try {
                        robot.computeRoadTo(new Coordinates(destx, desty));
                    } catch (UnlandedRobotException e) {
                        System.out.println("Impossible d'établir une route, le robot est encore en l'air");
                        break;
                    } catch (LandSensorDefaillance landSensorDefaillance) {
                        System.out.println("Allo Houston, on a un problème. On a perdu le retour sol");
                        break;
                    } catch (UndefinedRoadbookException e) {
                        System.out.println("Impossible d'établir une route, la destination est inatteignable");
                    }
                    try {
                        List<CheckPoint> checkPoints = robot.letsGo();
                        for (CheckPoint point : checkPoints) {
                            System.out.println("Position : (" + point.position.getX() + " ," + point.position.getY() + ") - orientation : " + point.direction);
                        }
                    } catch (UnlandedRobotException e) {
                        System.out.println("Le robot est encore en l'air, il doit se poser d'abord");
                    } catch (UndefinedRoadbookException e) {
                        System.out.println("Il semblerait que le road book soit manquant");
                    } catch (InsufficientChargeException e) {
                        System.out.println("Désolé, je n'ai pas suffisamment d'énergie pour terminer mon parcours");
                        displayBlackBox(robot.blackBox);
                    } catch (LandSensorDefaillance landSensorDefaillance) {
                        System.out.println("Allo Houston, on a un problème. On a perdu le retour sol");
                    } catch (InaccessibleCoordinate inaccessibleCoordinate) {
                        System.out.println("Je suis malheureusement dans l'impossibilité de rejoindre la destination demandée");
                    }
            }
        }
    }

    private static void displayBlackBox(BlackBox blackBox) {
        System.out.println("Contenu de la boite noire");
        for (CheckPoint point : blackBox.getCheckPointList()) {
            System.out.println("Position : (" + point.position.getX() + " ," + point.position.getY() + ") - orientation : " + point.direction + " mode commande : " + (point.manualDirective ? "manuel" : "automatique"));
        }
    }

    private static void displayMenu() {
        System.out.println("Panneau de commandes");
        System.out.println("A : poser le robot");
        System.out.println("Z : avancer");
        System.out.println("Q : tourner à gauche");
        System.out.println("D : tourner à droite");
        System.out.println("S : reculer");
        System.out.println("M : donner une coordonnée à atteindre");

    }
}
