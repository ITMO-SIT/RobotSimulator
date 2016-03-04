package simulator.services;

import org.w3c.dom.*;
import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.params.SimulationParam;
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

    private void loadXMLFile(String path) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
            NodeList nodes;

            nodes = doc.getElementsByTagName("SingleSimulation");
            for (int i = 0, len = nodes.getLength(); i < len; i++) {
                Element elm = (Element) nodes.item(i);
                HashMap<String, SimulationParam> params = parseParam(elm);
                params.putAll(parseClasses(elm));
                wrappers.add(new SingleSimulation(params));
            }

            nodes = doc.getElementsByTagName("RepeatSimulation");
            for (int i = 0, len = nodes.getLength(); i < len; i++) {
                Element elm = (Element) nodes.item(i);
                HashMap<String, SimulationParam> params = parseParam(elm);
                params.putAll(parseClasses(elm));
                wrappers.add(new RepeatSimulation(params));
            }

            nodes = doc.getElementsByTagName("InfinitySimulation");
            for (int i =0, len = nodes.getLength(); i < len; i++) {
                Element elm = (Element) nodes.item(i);
                HashMap<String, SimulationParam> params = parseParam(elm);
                params.putAll(parseClasses(elm));
                wrappers.add(new InfinitySimulation(params));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, SimulationParam> parseParam(Element node) {
        HashMap<String, SimulationParam> params = new HashMap<>();
        NodeList nodes = node.getChildNodes();
        for (int i = 0, len = nodes.getLength(); i < len; i++) {
            Node child = nodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE && !child.getNodeName().equals("Class")) {
                String key = child.getAttributes().getNamedItem("param").getTextContent();
                params.put(key, SimulationParam.createParamByNode(child));
            }
        }
        return params;
    }

    private  HashMap<String, SimulationParam> parseClasses(Element node) {
        HashMap<String, SimulationParam> params = new HashMap<>();
        NodeList nodes = node.getElementsByTagName("Class");
        for (int i = 0, len = nodes.getLength(); i < len; i++) {
            NamedNodeMap map = nodes.item(i).getAttributes();
            String path = map.getNamedItem("value").getTextContent();
            try {
                classStorage.loadClass(path);
                params.put(map.getNamedItem("type").getTextContent(),
                        SimulationParam.createParam(SimulationParam.ParamType.CONST,
                                SimulationParam.ValueType.STRING, path));
            } catch (ClassNotFoundException err) {
                System.out.println("ERROR: класс " + path + " не найден");
            }
        }
        return params;
    }
}
