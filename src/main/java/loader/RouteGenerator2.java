package loader;

import de.tudresden.sumo.cmd.Edge;
import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.objects.SumoStage;
import de.tudresden.sumo.objects.SumoStringList;
import util.ConnectionManager;
import de.tudresden.sumo.cmd.Simulation;


public class RouteGenerator2 {
    public static int routeidcounter = 0;
    public static ConnectionManager conn;
    public SumoStage routeStage;
    public SumoStringList edgeList;

    /**
     * generates random routes by looking through all edges until a valid connection is found.
     *
     * @throws Exception
     */
    public RouteGenerator2() throws Exception{
        routeStage = new SumoStage();
        edgeList = (SumoStringList) conn.dojobget(Edge.getIDList());
        int edgeListSize = edgeList.size();
        edgeListSize--;
        int firstRandomEdgeIndex;
        int secondRandomEdgeIndex;

        do{
            firstRandomEdgeIndex = (int)(Math.random()*edgeListSize);
            secondRandomEdgeIndex = (int)(Math.random()*edgeListSize);
            //prevent edge to edge assignment (maybe ok?)
            String firstRandomEdge = edgeList.get(firstRandomEdgeIndex);
            String secondRandomEdge = edgeList.get(secondRandomEdgeIndex);

            routeStage = (SumoStage) conn.dojobget(Simulation.findRoute( firstRandomEdge, secondRandomEdge, "DEFAULT_VEHTYPE", (int)conn.dojobget(Simulation.getCurrentTime()), 0));
        }while(routeStage.edges.isEmpty());

        conn.dojobset(Route.add("route"+routeidcounter, routeStage.edges));

        routeidcounter++;

    }
}

