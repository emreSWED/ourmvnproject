package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimulAutosP extends JPanel {
    List<Cars> carsList = new ArrayList<>();
    Timer timer;

    public SimulAutosP() {
        timer = new Timer(20, e -> {
            for (Cars car : carsList) {
                car.x += 2;
            }
            repaint();
        });
        timer.start();
    }

    public void addCar(Cars car){
        carsList.add(car);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Cars car : carsList){
            g.fillRect(car.x, car.y, 20, 10);
        }
    }


}

