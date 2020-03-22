package util;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;

public interface ParserPackageToMap {
    LinkedHashMap<Class<?>, List<Method>> getMapClassMethodsWithAnnotations(String packageName, Class[] annClasses);
}
