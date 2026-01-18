package GUI;

import loader.YCoordinateFlipper;
import model.MyVehicle;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class CarRenderer {

    private final double carLength = 4.5;
    private final double carWidth = 2.0;

    public Shape draw(Graphics2D g2d, MyVehicle car) {
        double rx = car.getX();
        double ry = YCoordinateFlipper.flipYCoords(car.getY());
        double angle = car.getAngle();

        AffineTransform original = g2d.getTransform();

        g2d.translate(rx, ry);
        g2d.rotate(Math.toRadians(angle));

        Rectangle2D.Double localShape = new Rectangle2D.Double(
                -carWidth / 2,
                -carLength / 2,
                carWidth,
                carLength
        );

        g2d.setColor(car.getColor());
        g2d.fill(localShape);

        g2d.setColor(Color.BLACK);
        g2d.draw(localShape);

        AffineTransform t = new AffineTransform();
        t.translate(rx, ry);
        t.rotate(Math.toRadians(angle));
        Shape worldShape = t.createTransformedShape(localShape);

        g2d.setTransform(original);

        return worldShape;
    }
}
