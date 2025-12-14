package loader;

public class YCoordinateFlipper {
    public static double yBound1;

    public static double flipYCoords(double yCoord){
        return (yBound1 - yCoord);
    }
}
