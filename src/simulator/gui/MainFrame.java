package simulator.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setSize(new Dimension(1000, 700));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        final RenderingField canvas  = new RenderingField();
        SimulationList simList = new SimulationList(canvas);

        add(simList, BorderLayout.EAST);
        canvas.setBorder(BorderFactory.createLineBorder(Color.RED));
        add(new JScrollPane(canvas));

        setVisible(true);
    }
}
