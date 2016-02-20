package simulator.services;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.simulation.wrapper.RepeatSimulation;
import simulator.simulation.wrapper.SimulationWrapper;
import simulator.simulation.wrapper.SingleSimulation;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SimulationController {

    private ArrayList<SimulationWrapper> wrappers;

    // TODO: объеденить в context и передовать wrapper'ам.
    private ThreadController threadController;
    private ClassStorage     classStorage;

    public SimulationController() {
        wrappers         = new ArrayList<>();
        threadController = new ThreadController();
        classStorage     = ClassStorage.getInstance();
        loadXMLFile("testConfiguration.xml");
    }


    public void start(int i) {
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

    private void loadXMLFile(String path) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
            NodeList nodes;

            nodes = doc.getElementsByTagName("SingleSimulation");
            for (int i = 0, len = nodes.getLength(); i < len; i++) {
                Element elm = (Element) nodes.item(i);
                HashMap<String, String> params = parseConstParam(elm);
                params.putAll(parseClasses(elm));
                wrappers.add(new SingleSimulation(params));
            }

            nodes = doc.getElementsByTagName("RepeatSimulation");
            for (int i = 0, len = nodes.getLength(); i < len; i++) {
                Element elm = (Element) nodes.item(i);
                HashMap<String, String> params = parseConstParam(elm);
                params.putAll(parseClasses(elm));
                wrappers.add(new RepeatSimulation(params));
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

    private HashMap<String, String> parseClasses(Element node) {
        HashMap<String, String> params = new HashMap<>();
        NodeList nodes = node.getElementsByTagName("Class");
        for (int i = 0, len = nodes.getLength(); i < len; i++) {
            NamedNodeMap map = nodes.item(i).getAttributes();
            String path = map.getNamedItem("value").getTextContent();
            params.put(map.getNamedItem("type").getTextContent(), path);
            try {
                classStorage.loadClass(path);
            } catch (ClassNotFoundException err) {
                System.out.println("ERROR: класс " + path + " не найден");
            }
        }
        return params;
    }
}
