package GUI;

import loader.LaneLoader;
import loader.MyLane;
import loader.YCoordinateFlipper;
import model.MyVehicle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class MapPanel extends JPanel {

    private List<MyVehicle> currentVehicles = new ArrayList<>(); //save list of Vehicles we want to draw

    //Camera system: for coordinate transformation from huge values in small window values
    private boolean boundsCalculated = false;
    private double scaleFactor = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;

    //definition of constants for color, for readability
    private final Color COLOR_BACKGROUND = new Color(34, 100, 34);
    private final Color COLOR_ROAD_BORDER = Color.WHITE;
    private final Color COLOR_ASPHALT = Color.BLACK;
    private final Color COLOR_DASH = Color.WHITE;
    private  Color COLOR_CAR = new Color(0, 255, 255);


    private final double LANE_WIDTH = 3.5;

    //constructor
    public MapPanel() {
        this.setBackground(COLOR_BACKGROUND);   //color of meadow
    }

    //interface to the outside, Main calls up this method, when new cars
    public void updateVehicles(List<MyVehicle> vehicles) {
        this.currentVehicles = vehicles; //save new data
        repaint();                       //tells when image is out of date. -> paintComponent
    }


    //where the drawing happens
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);                //Delete the old image
        Graphics2D g2d = (Graphics2D) g;        //The graphic object must be converted to 2D graphic


        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //aktiv Anti-Aliasing
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        //if no road is loaded, exit to prevent errors
        if (LaneLoader.myLanes == null || LaneLoader.myLanes.isEmpty()) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Waiting for simulation...", 50, 50);
            return;
        }

        //Calculate zoom factor and offset, to fit map to screen
        if (!boundsCalculated || getWidth() > 0) {
            calculateZoom();
        }

        AffineTransform old = g2d.getTransform();                       //Save the current standard camera state
        g2d.translate(offsetX, offsetY);                                //Offset, map in the middle
        g2d.scale(scaleFactor, scaleFactor);                            //Zoom to convert meters in pixel
        drawRoadLayer(g2d, COLOR_ROAD_BORDER, LANE_WIDTH + 1.5);
        drawRoadLayer(g2d, COLOR_ASPHALT, LANE_WIDTH);
        drawDashedLines(g2d);
        //drawStopLines(g2d);
        drawCars(g2d);
        g2d.setTransform(old);                                          //restore Zoom for swing
    }


    private void calculateZoom() {
        //initialize min/max with extrem values
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        boolean found = false;

        //Search for extrem coordinate (iterate through all lines for bounding Box of city network)
        for (MyLane lane : LaneLoader.myLanes) {
            if (lane.lanePath != null) {
                Rectangle2D b = lane.lanePath.getBounds2D();
                if (b.getWidth() > 0) {
                    if (b.getMinX() < minX) minX = b.getMinX();
                    if (b.getMinY() < minY) minY = b.getMinY();
                    if (b.getMaxX() > maxX) maxX = b.getMaxX();
                    if (b.getMaxY() > maxY) maxY = b.getMaxY();
                    found = true;
                }
            }
        }

        if (!found) return; // if no map data found, Stop

        // Calculate the dimensions of the map
        double mapW = maxX - minX;
        double mapH = maxY - minY;
        //plus margin for window
        double buffer = Math.max(mapW, mapH) * 0.1;

        // Calculate scaling factors for width and height
        double scaleX = getWidth() / (mapW + buffer);
        double scaleY = getHeight() / (mapH + buffer);

        // Use the smaller scale factor to fit the map in the window
        this.scaleFactor = Math.min(scaleX, scaleY);

        // Calculate offsets to center the map in the window
        this.offsetX = (getWidth() - (mapW * scaleFactor)) / 2 - (minX * scaleFactor);
        this.offsetY = (getHeight() - (mapH * scaleFactor)) / 2 - (minY * scaleFactor);

        //mark as calculated
        this.boundsCalculated = true;
    }

    //draw road
    private void drawRoadLayer(Graphics2D g2d, Color c, double width) {
        g2d.setColor(c);

        g2d.setStroke(new BasicStroke((float)width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (MyLane lane : LaneLoader.myLanes) {
            if (lane.lanePath != null) g2d.draw(lane.lanePath);
        }
    }

    private void drawDashedLines(Graphics2D g2d) {
        g2d.setColor(COLOR_DASH);

        float[] dash = {3.0f, 6.0f};
        g2d.setStroke(new BasicStroke(0.3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));

        for (MyLane lane : LaneLoader.myLanes) {
            if (lane.lanePath != null) g2d.draw(lane.lanePath);
        }
    }

    /*private void drawStopLines(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(0.6f));

        for (MyLane lane : LaneLoader.myLanes) {
            //2 points needed for direction of lines
            if (lane.xpositions != null && lane.xpositions.length > 1) {
                int last = lane.xpositions.length - 1;

                //get coordinates of last segment of the lane
                double endX = lane.xpositions[last];
                double endY = lane.ypositions[last];
                double prevX = lane.xpositions[last - 1];
                double prevY = lane.ypositions[last - 1];

                //calculating direction of the road
                double dx = endX - prevX;
                double dy = endY - prevY;
                double len = Math.sqrt(dx*dx + dy*dy);


                if (len < 0.1) continue;

                dx /= len; dy /= len;
                double perpX = -dy; double perpY = dx;


                double w = LANE_WIDTH * 0.45;

                g2d.draw(new Line2D.Double(
                        endX - perpX * w, endY - perpY * w,
                        endX + perpX * w, endY + perpY * w
                ));
            }
        }
    }*/

    private void drawCars(Graphics2D g2d) {

        g2d.setColor(SumoTrafficControl.getCarColor());

        double carLength = 4.5;
        double carWidth = 2.0;

        for (MyVehicle car : currentVehicles) {
            double rx = car.getX();

            //hier we have to flip the coordinates
            double ry = YCoordinateFlipper.flipYCoords(car.getY());
            double angle = car.getAngle(); // 0 = North  90 = East

            //save camera state
            AffineTransform original = g2d.getTransform();

            //move pointer to car position
            g2d.translate(rx, ry);
            //rotate our "rectangle" around the car's center
            g2d.rotate(Math.toRadians(angle));

            //define car shape centered (0,0)
            Rectangle2D.Double carShape = new Rectangle2D.Double(
                    -carWidth / 2, -carLength / 2, carWidth, carLength
            );

            //Draw car body
            g2d.fill(carShape);

            //Draw outline for visibility
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(0.2f));
            g2d.draw(carShape);

            //Reset color and reset camera for next car
            g2d.setColor(SumoTrafficControl.getCarColor());
            g2d.setTransform(original);
        }
    }
}