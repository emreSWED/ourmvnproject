package util;

import de.tudresden.sumo.cmd.*;
import de.tudresden.sumo.util.*;
import it.polito.appeal.traci.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.MyVehicle;
import model.MyTrafficLight;

public class ConnectionManager {
    private String configFile;
    public static SumoTraciConnection traciConnection;

    public ConnectionManager(String configFile) {
        this.configFile = configFile;
        traciConnection = new SumoTraciConnection("sumo-gui", this.configFile);
    }

    public Object dojobget(SumoCommand cmd) throws Exception {

        return traciConnection.do_job_get(cmd);
    }

    public void dojobset(SumoCommand cmd) throws Exception {
        traciConnection.do_job_set(cmd);
    }

    public void startConnection() throws IOException {
        traciConnection.addOption("start","true");
        traciConnection.runServer();
    }

    public void stopConnection() {
        traciConnection.close();
    }

    public void step() throws Exception {
        traciConnection.do_timestep();
    }
}