package loader;

import java.util.ArrayList;
import java.util.List;

public class LaneTrafficLight {
    public static int idnumber = 0;
    public int id;
    public String fromLane;
    public Double xpos;
    public Double ypos;
    public List<SingleTrafficLight> singleTrafficLights = new ArrayList<SingleTrafficLight>();

    public LaneTrafficLight(List<SingleTrafficLight> singleTrafficLights) {
        this.singleTrafficLights.addAll(singleTrafficLights);
        this.id = idnumber++;
        this.fromLane = singleTrafficLights.getFirst().fromLane;
        this.xpos = singleTrafficLights.getFirst().xpos;
        this.ypos = singleTrafficLights.getFirst().ypos;
    }

    public void setGreen(){
        for(SingleTrafficLight singleTrafficLight : singleTrafficLights){
            singleTrafficLight.setGreenNonPriority();
        }
    }

    public void setYellow(){
        for(SingleTrafficLight singleTrafficLight : singleTrafficLights){
            singleTrafficLight.setYellow();
        }
    }

    public void setRed(){
        for(SingleTrafficLight singleTrafficLight : singleTrafficLights){
            singleTrafficLight.setRed();
        }
    }
}
