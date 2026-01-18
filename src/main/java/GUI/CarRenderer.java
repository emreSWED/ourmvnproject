package GUI;

import loader.YCoordinateFlipper;
import model.MyVehicle;
import util.MySystem;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class CarRenderer {

    private final double carLength = 4.8;
    private final double carWidth = 2.0;

    public Shape draw(Graphics2D g2d, MyVehicle car) {

        double rx = car.getX();
        double ry = YCoordinateFlipper.flipYCoords(car.getY());
        double angle = car.getAngle();

        AffineTransform original = g2d.getTransform();

        g2d.translate(rx, ry);
        g2d.rotate(Math.toRadians(angle));

        RoundRectangle2D.Double body = new RoundRectangle2D.Double(
                -carWidth / 2, -carLength / 2,
                carWidth, carLength,
                0.5, 0.5
        );

        g2d.setColor(car.getColor());
        g2d.fill(body);

        if (MySystem.selectedVehicles.contains(car)) {  // if car is selected
            g2d.setColor(new Color(255, 0, 255, 120));
            g2d.fill(new Ellipse2D.Double(
                    -carWidth,
                    -carLength,
                    carWidth * 2,
                    carLength * 2
            ));
        }


        g2d.setColor(new Color(163, 195, 230, 200));

        double windowInset = 0.3;
        double windowHeight = 0.8;
        double windowY = -carLength / 2 + 0.8;

        Rectangle2D.Double windshield = new Rectangle2D.Double(
                -carWidth / 2 + windowInset,
                windowY,
                carWidth - 2 * windowInset,
                windowHeight
        );
        g2d.fill(windshield);

        double rearWindowY = carLength / 2 - 1.0;
        Rectangle2D.Double rearWindow = new Rectangle2D.Double(
                -carWidth / 2 + windowInset,
                rearWindowY,
                carWidth - 2 * windowInset,
                0.6
        );
        g2d.fill(rearWindow);

        g2d.setTransform(original);

        AffineTransform t = new AffineTransform();
        t.translate(rx, ry);
        t.rotate(Math.toRadians(angle));

        return t.createTransformedShape(body);
    }
}
