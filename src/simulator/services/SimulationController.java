package simulator.services;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.simulation.wrapper.InfinitySimulation;
import simulator.simulation.wrapper.RepeatSimulation;
import simulator.simulation.wrapper.SimulationWrapper;
import simulator.simulation.wrapper.SingleSimulation;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SimulationController {

    private ArrayList<SimulationWrapper> wrappers;
    private ThreadController threadController;

    public SimulationController() {
        wrappers = new ArrayList<>();
        threadController = new ThreadController();
        parseXMLFile("testConfiguration.xml");
    }


    public void start(int i) {
        SimulationWrapper temp = wrappers.get(i);
        if (temp instanceof InfinitySimulation) {
            ((InfinitySimulation) temp).activateWrapper();      // fixme
        }
        threadController.add(wrappers.get(i));
    }

    public void pause(int i) {
        wrappers.get(i).pause();
    }

    public Observable addObserver(int i, Observer observer) {
        return wrappers.get(i).addSimulationObserver(observer);
    }

    public ArrayList<SimulationWrapper> getWrappers() {
        return wrappers;
    }

//---------------------------------------------------------------------------------//

    private void parseXMLFile(String path) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
            NodeList nodes;

            nodes = doc.getElementsByTagName("SingleSimulation");
            for (int i =0, len = nodes.getLength(); i < len; i++) {
                Element elm = (Element) nodes.item(i);
                wrappers.add(new SingleSimulation(parseConstParam(elm)));
            }

            nodes = doc.getElementsByTagName("RepeatSimulation");
            for (int i =0, len = nodes.getLength(); i < len; i++) {
                Element elm = (Element) nodes.item(i);
                wrappers.add(new RepeatSimulation(parseConstParam(elm)));
            }

            nodes = doc.getElementsByTagName("InfinitySimulation");
            for (int i =0, len = nodes.getLength(); i < len; i++) {
                Element elm = (Element) nodes.item(i);
                wrappers.add(new InfinitySimulation(parseConstParam(elm)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, String> parseConstParam(Element node) {
        HashMap<String, String> params = new HashMap<>();
        NodeList nodes = node.getElementsByTagName("ConstParam");
        for (int i = 0, len = nodes.getLength(); i < len; i++) {
            NamedNodeMap map = nodes.item(i).getAttributes();
            params.put(map.getNamedItem("param").getTextContent(), map.getNamedItem("value").getTextContent());
        }
        return params;
    }
}
