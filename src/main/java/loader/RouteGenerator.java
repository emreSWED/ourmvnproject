package loader;

import de.tudresden.sumo.cmd.Edge;
import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.objects.SumoStage;
import de.tudresden.sumo.objects.SumoStringList;
import util.ConnectionManager;
import de.tudresden.sumo.cmd.Simulation;

import java.util.ArrayList;
import java.util.List;

public class RouteGenerator {
    public static ConnectionManager conn;
    public SumoStage routeStage0,routeStage1,routeStage2,routeStage3,routeStage4;
    public SumoStringList edgeList;

    public RouteGenerator() throws Exception {

        routeStage0 = new SumoStage();

        edgeList = (SumoStringList) conn.dojobget(Edge.getIDList());


        //for Future this will be able to generate routes based on two edges which can be grabbed from edgeList
        routeStage0 = (SumoStage)conn.dojobget(Simulation.findRoute("495683152", "317948763"  ,"DEFAULT_VEHTYPE", (int)conn.dojobget(Simulation.getCurrentTime()), 0));
        routeStage1 = (SumoStage)conn.dojobget(Simulation.findRoute("-317948763", "5218604"  ,"DEFAULT_VEHTYPE", (int)conn.dojobget(Simulation.getCurrentTime()), 0));
        routeStage2 = (SumoStage)conn.dojobget(Simulation.findRoute("23490810#1", "317948763"  ,"DEFAULT_VEHTYPE", (int)conn.dojobget(Simulation.getCurrentTime()), 0));
        routeStage3 = (SumoStage)conn.dojobget(Simulation.findRoute("5218604", "317948763"  ,"DEFAULT_VEHTYPE", (int)conn.dojobget(Simulation.getCurrentTime()), 0));
        routeStage4 = (SumoStage)conn.dojobget(Simulation.findRoute("495683152", "5218604"  ,"DEFAULT_VEHTYPE", (int)conn.dojobget(Simulation.getCurrentTime()), 0));

        conn.dojobset(Route.add("route0", routeStage0.edges));
        System.out.println("Generate route0 from SumoStage object to Route object:" + routeStage0.edges);

        conn.dojobset(Route.add("route25", routeStage1.edges));
        System.out.println("Generate route1 from SumoStage object to Route object:" + routeStage1.edges);

        conn.dojobset(Route.add("route2", routeStage2.edges));
        System.out.println("Generate route2 from SumoStage object to Route object:" + routeStage2.edges);

        conn.dojobset(Route.add("route3", routeStage3.edges));
        System.out.println("Generate route3 from SumoStage object to Route object:" + routeStage3.edges);

        conn.dojobset(Route.add("route4", routeStage4.edges));
        System.out.println("Generate route4 from SumoStage object to Route object:" + routeStage4.edges);


        //Route.add("route0", )
    }
}
