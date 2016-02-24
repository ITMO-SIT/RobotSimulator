package simulator.helper.params;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


// Если потребуется расширять, то посмотри в сторону groovy
// А лучше и просто попробовать переделать под groovy и потестить

abstract public class SimulationParam {

    protected enum Type {INT, LONG, FLOAT, DOUBLE, STRING, ERROR;
        public static Type parseString(String str) {
            switch (str) {
                case "int"    : return INT;
                case "long"   : return LONG;
                case "float"  : return FLOAT;
                case "double" : return DOUBLE;
                case "string" : return STRING;
                default: return ERROR;
            }
        }
        public static Object parseValue(Type type, String str) {
            switch (type) {
                case INT:    return Integer.parseInt(str);
                case LONG:   return Long.parseLong(str);
                case FLOAT:  return Float.parseFloat(str);
                case DOUBLE: return Double.parseDouble(str);
                case STRING: return str;
                default: return null;
            }
        }
    }
    protected Type type;

    public abstract <T> T getValue();

    @SuppressWarnings("unchecked")
    public static SimulationParam createParam(Node node) {
        SimulationParam newObject = null;
        NamedNodeMap attr = node.getAttributes();
        Type type = Type.parseString(attr.getNamedItem("type").getTextContent());
        switch (node.getNodeName()) {
            case "ConstParam" :
                newObject = new ConstParam(Type.parseValue(type, attr.getNamedItem("value").getTextContent()));
                break;
            case "RandomParam" :
                Object min = Type.parseValue(type, attr.getNamedItem("min_val").getTextContent());
                Object max = Type.parseValue(type, attr.getNamedItem("max_val").getTextContent());
                newObject = new RandomParam(type, min, max);
                break;
        }
        return newObject;
    }

}
