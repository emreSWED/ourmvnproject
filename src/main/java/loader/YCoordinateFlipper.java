package loader;

public class YCoordinateFlipper {
    /**
     * ybound of simulation window, needed for flipping coordinates for swing rendering
     */
    public static double yBound1;

    /**
     * flips coordinates through the middle, used for swing rendering
     * @param yCoord coordinate to be flipped
     * @return the flipped coordinate
     */

    public static double flipYCoords(double yCoord){
        return (yBound1 - yCoord);
    }
}
