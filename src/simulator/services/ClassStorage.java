package simulator.services;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ClassStorage {
    // TODO: почитать про ClassLoader
    private final static ClassStorage instance = new ClassStorage();
    public static ClassStorage getInstance() {return instance;}
//--------------------------------------------------------------//

    private HashMap<String, Class<?>> classes;

    private ClassStorage() {
        classes = new HashMap<>();
    }

    public Object newClassInstance(String path)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        Class temp_class = classes.get(path);
        if (temp_class == null)
            temp_class = loadClass(path);
        return temp_class.newInstance();
    }

    public List<Object> newClassInstance(String path, int count)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        List<Object> result = new LinkedList<>();
        Class temp_class = classes.get(path);
        if (temp_class == null)
            temp_class = loadClass(path);
        for (int i = 0; i < count; i++) {
            result.add(temp_class.newInstance());
        }
        return result;
    }

    public List<Field> getFieldWithAnnotation(String path, Annotation annotation) throws ClassNotFoundException {
        List<Field> result = new LinkedList<>();
        Class temp_class = classes.get(path);
        if (temp_class == null)
            temp_class = loadClass(path);
        for (Field field : temp_class.getFields()) {
            if (field.isAnnotationPresent(annotation.getClass()))
                result.add(field);
        }
        return result;
    }

    private Class loadClass(String path) throws ClassNotFoundException {
        Class temp_class = Class.forName(path);
        classes.put(path, temp_class);
        return temp_class;
    }

}
