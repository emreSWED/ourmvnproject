package GUI;

import loader.LaneLoader;
import model.MyLane;
import loader.YCoordinateFlipper;
import model.MyVehicle;
import util.MySystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapPanel extends JPanel {

    private List<MyVehicle> currentVehicles = new ArrayList<>(); //save list of Vehicles we want to draw
    private final Map<MyVehicle, Shape> carHitboxes = new HashMap<>();
    private final CarRenderer carRenderer = new CarRenderer();

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

    private final double LANE_WIDTH = 3.5;

    //constructor
    public MapPanel() {
        this.setBackground(COLOR_BACKGROUND);   //color of meadow

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    AffineTransform cam = new AffineTransform();
                    cam.translate(offsetX, offsetY);
                    cam.scale(scaleFactor, scaleFactor);

                    Point2D worldPoint = cam.createInverse().transform(e.getPoint(), null);

                    boolean shift = e.isShiftDown();

                    for (Map.Entry<MyVehicle, Shape> entry : carHitboxes.entrySet()) {
                        if (entry.getValue().contains(worldPoint)) {
                            MyVehicle car = entry.getKey();
                            System.out.println("Clicked on car: " + car.getId());

                            if (!shift) {
                                MySystem.selectedVehicles.clear();
                                MySystem.selectedVehicles.add(car);
                            } else {
                                if (!MySystem.selectedVehicles.remove(car)) {
                                    MySystem.selectedVehicles.add(car);
                                }
                            }

                            System.out.println("All selected cars: " + MySystem.selectedVehicles);
                            repaint();
                            return;
                        }
                    }

                    if (!shift) {
                        MySystem.selectedVehicles.clear();
                        System.out.println("Deselected all cars");
                        repaint();
                    } else
                        System.out.println("Nothing happened");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        InputMap im = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), "selectAll");

        am.put("selectAll", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MySystem.selectedVehicles.clear();
                MySystem.selectedVehicles.addAll(currentVehicles);
                System.out.println("Selected all car: " + MySystem.selectedVehicles);
                repaint();
            }
        });
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

    private void drawCars(Graphics2D g2d) {

        carHitboxes.clear();

        for (MyVehicle car : currentVehicles) {
            Shape worldShape = carRenderer.draw(g2d, car);
            carHitboxes.put(car, worldShape);
        }
    }
}
