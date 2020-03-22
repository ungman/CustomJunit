package util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomRunTestPrepareData {

    private String packageName;
    private ParserPackageToMap parserPackageToMap;
    private Map<Class<?>, List<Method>> mapClassMethods;
    private List<Method> methodList;

    public CustomRunTestPrepareData(String packageName, ParserPackageToMap parserPackageToMap, Class[] classAnnotations) {
        this.parserPackageToMap = parserPackageToMap;
        this.getMapClassMethodsWithAnnotations(classAnnotations);
    }


    private void getMapClassMethodsWithAnnotations(Class[] classAnnotations) {
        mapClassMethods = parserPackageToMap.getMapClassMethodsWithAnnotations(packageName, classAnnotations);
    }

    public List<Method> getListMethodWithAnnotation(Class currentClass, Class classAnnotation) {
        return mapClassMethods.get(currentClass).stream()
                .filter(method -> method.getAnnotation(classAnnotation) != null)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
