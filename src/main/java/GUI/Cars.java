package GUI;
import model.MyVehicle;

public class Cars {
    private static int carsCount = 0;
    private int carId;
    int x = 0,y = 0;

    //constructor

    Cars(int x, int y){
        carsCount++;
        this.x =x;
        this.y =y;

        carId = carsCount;
    }

    //methods
    public int getCarId(){
        return carId;
    }

    public static int getCarsCount() {
        return carsCount;
    }

}
