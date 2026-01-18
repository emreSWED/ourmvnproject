package loader;

import de.tudresden.sumo.objects.SumoLink;
import model.MyTrafficLight;

public class SingleTrafficLight {
    public String ID;
    public double xpos;
    public double ypos;
    public MyTrafficLight myTrafficLight;
    public SumoLink sumoLink;
    public String fromLane;
    public String throughLane;
    public String toLane;

    public int index;


    public StringBuilder clusterState;
    public int clusterStateLenght;
    /**
     * This simulates single traffic light control, it still controls the traffic light as a Cluster by using its State, but only changes the corresponding index in the State
     * @param myTrafficLight TrafficLightSplitter class iterates through a list of MyTrafficLight objects and uses the current traffic light as its parameter
     * @param index TrafficLightSplitter class gives the current index of the Single traffic light it iterates through
     */
    public SingleTrafficLight(MyTrafficLight myTrafficLight, int index, String ID) {

        try {
            this.ID = ID;
            this.myTrafficLight = myTrafficLight;
            this.index = index;
            clusterState = new StringBuilder(myTrafficLight.getState());

            clusterStateLenght = clusterState.length();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setGreen(){
        try {
            clusterState = new StringBuilder(myTrafficLight.getState().toString()); //updates clusterState
            clusterState.setCharAt(index, 'G'); //Sets specific lane -> lane traffic light to green (only for turning left for example);
            myTrafficLight.setState(clusterState.toString()); //updates the traffic light cluster;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setGreenNonPriority(){
        try {
            clusterState = new StringBuilder(myTrafficLight.getState());
            clusterState.setCharAt(index, 'g');
            myTrafficLight.setState(clusterState.toString());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setYellow(){
        try {
            clusterState = new StringBuilder(myTrafficLight.getState().toString());
            clusterState.setCharAt(index, 'y');
            myTrafficLight.setState(clusterState.toString());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setRed(){
        try {
            clusterState = new StringBuilder(myTrafficLight.getState().toString());
            clusterState.setCharAt(index, 'r');
            myTrafficLight.setState(clusterState.toString());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
