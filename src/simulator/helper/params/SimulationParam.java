package simulator.helper.params;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


// Если потребуется расширять, то посмотри в сторону groovy
// А лучше и просто попробовать переделать под groovy и потестить

abstract public class SimulationParam {

    public enum ParamType {CONST, RANDOM}
    public enum ValueType {INT, LONG, FLOAT, DOUBLE, STRING, ERROR;
        public static ValueType parseString(String str) {
            switch (str) {
                case "int"    : return INT;
                case "long"   : return LONG;
                case "float"  : return FLOAT;
                case "double" : return DOUBLE;
                case "string" : return STRING;
                default: return ERROR;
            }
        }
        public static Object parseValue(ValueType valueType, String str) {
            switch (valueType) {
                case INT:    return Integer.parseInt(str);
                case LONG:   return Long.parseLong(str);
                case FLOAT:  return Float.parseFloat(str);
                case DOUBLE: return Double.parseDouble(str);
                case STRING: return str;
                default: return null;
            }
        }
    }
    protected ValueType valueType;

    public abstract <T> T getValue();

    // Было бы хорошо отвязать фабрику от входных параметов типа Node
    @SuppressWarnings("unchecked")
    public static SimulationParam createParamByNode(Node node) {
        SimulationParam newObject = null;
        NamedNodeMap attr = node.getAttributes();
        ValueType valueType = ValueType.parseString(attr.getNamedItem("type").getTextContent());
        switch (node.getNodeName()) {
            case "ConstParam" :
                newObject = new ConstParam(ValueType.parseValue(valueType, attr.getNamedItem("value").getTextContent()));
                break;
            case "RandomParam" :
                Object min = ValueType.parseValue(valueType, attr.getNamedItem("min_val").getTextContent());
                Object max = ValueType.parseValue(valueType, attr.getNamedItem("max_val").getTextContent());
                newObject = new RandomParam(valueType, min, max);
                break;
        }
        return newObject;
    }

    public static SimulationParam createParam(ParamType paramType, ValueType valueType, Object ... values) {
        SimulationParam newObject = null;
        switch (paramType) {
            case CONST:
                newObject = new ConstParam(values[0]);
                break;
            case RANDOM:
                newObject = new RandomParam(valueType, values[0], values[1]);
                break;
        }
        return newObject;
    }

}
