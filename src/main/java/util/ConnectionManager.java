package util;

import de.tudresden.sumo.cmd.*;
import de.tudresden.sumo.util.*;
import it.polito.appeal.traci.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.MyVehicle;
import model.MyTrafficLight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionManager {
    private static final Logger LOG = LogManager.getLogger(ConnectionManager.class.getName());


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
        LOG.info("Starting SUMO TraCI connection using config {}",configFile);
        traciConnection.addOption("start","true");
        traciConnection.runServer();
    }

    public void stopConnection() {
        LOG.info("Connection closed.");
        traciConnection.close();
    }

    public void step() throws Exception {
        traciConnection.do_timestep();
    }
}