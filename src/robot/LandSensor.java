package robot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LandSensor {

    private Map<Coordinates, Land> carte;

    private Random random;

    public LandSensor(Random random) {
        carte = new HashMap<Coordinates, Land>();
        this.random = random;
    }

    public double getPointToPointEnergyCoefficient(Coordinates coordinate1, Coordinates coordinate2) throws LandSensorDefaillance, InaccessibleCoordinate {
        if (carte.get(coordinate1)==null)
            try {
                carte.put(coordinate1, Land.getLandByOrdinal(random.nextInt(Land.CountLand())));
            } catch (TerrainNonRepertorieException e) {
                throw new LandSensorDefaillance();
            }
        if (carte.get(coordinate2)==null)
            try {
                carte.put(coordinate2, Land.getLandByOrdinal(random.nextInt(Land.CountLand())));
            } catch (TerrainNonRepertorieException e) {
                throw new LandSensorDefaillance();
            }
        Land terrain1 = carte.get(coordinate1);
        Land terrain2 = carte.get(coordinate2);
        if (terrain2==Land.Infranchissable) throw new InaccessibleCoordinate(coordinate2);
        return (terrain1.coefficient+terrain2.coefficient)/2.0;
    }

}
