package simulator.gui;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import simulator.configuration.Configuration;
import simulator.simulation.Simulation;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SimulationList extends JPanel{

    private ArrayList<SimulationListItem> simulations;

    public SimulationList() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        simulations = new ArrayList<>();

        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(new File("testConfiguration.xml"));
            NodeList testList = doc.getElementsByTagName("testConfiguration");

            Configuration conf = Configuration.getInstance();
            for (int i = 0, len = testList.getLength(); i < len; i++) {
                Simulation sim = conf.newSimulationInstance();
                sim.setSimulationParam(parseSimulationParam(testList.item(i)));
                sim.init();
                addSimulation(sim);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSimulation(Simulation sim) {
        SimulationListItem listItem = new SimulationListItem(sim);
        simulations.add(listItem);
        add(listItem);
    }

    public void addItemMouseListener(MouseAdapter adapter) {
        for (SimulationListItem item : simulations)
            item.addMouseListener(adapter);
    }

    public void selectItem(SimulationListItem selectedItem) {
        for (SimulationListItem item : simulations)
            item.setBackground(Color.WHITE);
        selectedItem.setBackground(Color.LIGHT_GRAY);
    }

    private HashMap<String, String> parseSimulationParam(Node node) {
        HashMap<String, String> param = new HashMap<>();
        NodeList child = node.getChildNodes();
        for (int i = 0, len = child.getLength(); i < len; i++) {
            Node n = child.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                String key = n.getNodeName();
                String val = n.getAttributes().getNamedItem("value").getTextContent();
                param.put(key, val);
            }
        }
        return param;
    }

}
