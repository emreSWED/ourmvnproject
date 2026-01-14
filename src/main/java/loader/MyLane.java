package loader;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import util.ConnectionManager;
import de.tudresden.sumo.cmd.Lane;
import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.objects.SumoGeometry;

//LaneLoader and MyLane classes work together to create all the instances of MyLane objects containing Path2D which is essential for rendering on Graphics2D
//FOR RENDERING Path2D is ready to be used with y flipped coordinates
public class MyLane {
    public String laneID;

    public Path2D lanePath; //Creates a Path2D object for Graphics2D rendering in our own gui
    public boolean hasTrafficLight; //info wether this lane has a traffic light at its end
    public static ConnectionManager conn;


   public double[] xpositions;
   public double[] ypositions;
   public double[] testing;



    public MyLane(String laneID, int lanePointSize) throws Exception {
        this.laneID = laneID;
        this.xpositions = new double[lanePointSize];
        this.ypositions = new double[lanePointSize];
        this.lanePath = new Path2D.Double();
        //System.out.println("laneCoordinates of lane: " + laneID + " are: " + conn.dojobget(Lane.getShape(laneID)));
        SumoGeometry geometry = (SumoGeometry) conn.dojobget(Lane.getShape(laneID)); //SumoGeometry is an object made up of LinkedList<SumoPoint2D>
        int currentListSize = geometry.coords.size();
        for(int i = 0; i < currentListSize; i++){
            xpositions[i] = geometry.coords.get(i).x;
            ypositions[i] = YCoordinateFlipper.flipYCoords(geometry.coords.get(i).y); //Sumo has 0,0 as BOTTOM LEFT; where as Graphics2D renders with TOP LEFT
        }
        //loads all the coordinates into an array for much simpler handling
        System.out.println("creating Path2D object for rendering for lane: " + laneID + "..."); //todo LOGGER
        for(int i = 0; i < currentListSize; i++){
            if(i == 0){
                lanePath.moveTo(xpositions[i], ypositions[i]);
            }
            else{
                lanePath.lineTo(xpositions[i], ypositions[i]);
            }
        }
        lanePath.closePath();

        System.out.println("Path2D object created."); //todo LOGGER
    }
}
