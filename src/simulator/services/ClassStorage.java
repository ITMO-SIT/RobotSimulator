package simulator.services;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;

public class ClassStorage {
    // TODO: почитать про ClassLoader
    // TODO: избавиться от singletone
    private final static ClassStorage instance = new ClassStorage();
    public static ClassStorage getInstance() {return instance;}
//--------------------------------------------------------------//

    private HashMap<String, Class<?>> classes;

    private ClassStorage() {
        classes = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T> T newClassInstance(String path)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, ClassCastException {

        Class temp_class = loadClass(path);
        return (T) temp_class.newInstance();
    }

    @SuppressWarnings("unchecked")
    public <T> LinkedList<T> newClassInstance(String path, int count)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, ClassCastException {

        LinkedList<T> result = new LinkedList<>();
        Class temp_class = loadClass(path);
        for (int i = 0; i < count; i++)
            result.add((T) temp_class.newInstance());
        return result;
    }

    public LinkedList<Field> getFieldWithAnnotation(String path, Class<? extends Annotation> annotation)
            throws ClassNotFoundException {

        LinkedList<Field> result = new LinkedList<>();
        Class temp_class = loadClass(path);

        do {
            for (Field field : temp_class.getDeclaredFields())
                if (field.isAnnotationPresent(annotation))
                    result.add(field);
        } while ((temp_class = temp_class.getSuperclass()) != null);

        return result;
    }

    public Class loadClass(String path) throws ClassNotFoundException {
        Class temp_class;
        if (!classes.containsKey(path)) {
            temp_class = Class.forName(path);
            classes.put(path, temp_class);
        } else
            temp_class = classes.get(path);
        return temp_class;
    }

}
