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
        Land terrain1 = lazyGet(coordinate1);
        Land terrain2 = lazyGet(coordinate2);
        if (terrain2==Land.Infranchissable) throw new InaccessibleCoordinate(coordinate2);
        return (terrain1.coefficient+terrain2.coefficient)/2.0;
    }

    private Land lazyGet(Coordinates coordinate1) throws LandSensorDefaillance {
        if (carte.get(coordinate1)==null)
            try {
                carte.put(coordinate1, Land.getLandByOrdinal(random.nextInt(Land.countLand())));
            } catch (TerrainNonRepertorieException e) {
                throw new LandSensorDefaillance();
            }
        return carte.get(coordinate1);
    }

    public boolean isAccessible(Coordinates coordinates) throws LandSensorDefaillance {
        return lazyGet(coordinates) != Land.Infranchissable;
    }
}
