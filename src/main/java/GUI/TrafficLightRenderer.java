package GUI;

import loader.LaneLoader;
import model.MyLane;
import model.MyTrafficLight;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class TrafficLightRenderer {

    private final double LANE_WIDTH = 3.5;

    public void draw(Graphics2D g2d, MyTrafficLight tl) {

        try {
            String state = tl.getState();
            List<String> controlledLanes = tl.getControlledLanes();

            int loopLimit = Math.min(state.length(), controlledLanes.size());

            for (int i = 0; i < loopLimit; i++) {
                char signalChar = state.charAt(i);
                String laneID = controlledLanes.get(i);
                MyLane lane = LaneLoader.getLaneById(laneID);

                if (lane != null && lane.xpositions.length > 1) {
                    int last = lane.xpositions.length - 1;

                    double xEnd = lane.xpositions[last];
                    double yEnd = lane.ypositions[last];
                    double xPrev = lane.xpositions[last - 1];
                    double yPrev = lane.ypositions[last - 1];

                    double dx = xEnd - xPrev;
                    double dy = yEnd - yPrev;
                    double len = Math.sqrt(dx * dx + dy * dy);

                    if (len < 0.1) continue;
                    dx /= len;
                    dy /= len;

                    double w = 1.6;
                    double h = 4.5;

                    double offsetSide = LANE_WIDTH * 0.8 + 1.2;
                    double offsetBack = h / 2.0;

                    double lightX = xEnd + (-dy * offsetSide) - (dx * offsetBack);
                    double lightY = yEnd + (dx * offsetSide) - (dy * offsetBack);

                    double angle = Math.atan2(dy, dx);

                    AffineTransform old = g2d.getTransform();
                    g2d.translate(lightX, lightY);
                    g2d.rotate(angle + Math.PI / 2);

                    g2d.setColor(Color.DARK_GRAY);
                    g2d.fill(new RoundRectangle2D.Double(-w/2, -h/2, w, h, 0.5, 0.5));
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(0.1f));
                    g2d.draw(new RoundRectangle2D.Double(-w/2, -h/2, w, h, 0.5, 0.5));

                    Color cRed = new Color(50, 0, 0);
                    Color cYellow = new Color(50, 50, 0);
                    Color cGreen = new Color(0, 50, 0);

                    switch (Character.toLowerCase(signalChar)) {
                        case 'r': cRed = Color.RED; break;
                        case 'y': cYellow = Color.YELLOW; break;
                        case 'g': cGreen = Color.GREEN; break;
                    }

                    double rSize = 1.0;
                    double xPos = -rSize / 2;

                    g2d.setColor(cRed);
                    g2d.fill(new Ellipse2D.Double(xPos, -h/2 + 0.5, rSize, rSize));

                    g2d.setColor(cYellow);
                    g2d.fill(new Ellipse2D.Double(xPos, -rSize/2, rSize, rSize));

                    g2d.setColor(cGreen);
                    g2d.fill(new Ellipse2D.Double(xPos, h/2 - 0.5 - rSize, rSize, rSize));

                    g2d.setTransform(old);
                }
            }
        } catch (Exception e) { }
    }
}
