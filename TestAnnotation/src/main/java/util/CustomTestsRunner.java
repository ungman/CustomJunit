package util;

import util.annotation.After;
import util.annotation.Before;
import util.annotation.PrintTestResult;
import util.annotation.Test;
import util.asserts.DefaultAssertCounter;
import util.asserts.AssertsCounter;
import util.marker.IgnoreTestException;
import util.reflection.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomTestsRunner {

    private static final Class[] defaultAnnotation = new Class[]{After.class, Before.class, Test.class};
    private static final ParserPackageToMap ParserPackageToMap = ReflectionHelper.getInstance();
    private static final AssertsCounter testAssertCounter = DefaultAssertCounter.getInstance();
    private static CustomTestsRunner customTestsRunner = null;


    private CustomTestsRunner() {
    }

    public static void runTest(String packageName) {

        if (customTestsRunner == null)
            customTestsRunner = new CustomTestsRunner();

        Map<Class<?>, List<Method>> mapCLassMethod = ParserPackageToMap.getMapClassMethodsWithAnnotations(packageName, defaultAnnotation);
        for (Map.Entry<Class<?>, List<Method>> entry : mapCLassMethod.entrySet()) {
            customTestsRunner.invokingTestOnClass(entry);
        }
    }

    private static List<Method> getListMethodWithAnnotation(List<Method> listMethod, Class classAnnotations) {
        return listMethod.stream()
                .filter(method -> method.getAnnotation(classAnnotations) != null)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void invokingTestOnClass(Map.Entry<Class<?>, List<Method>> entry) {

        entry.getKey().getDeclaredConstructors();
        Object invokedObject = null;

        try {
            invokedObject = entry.getKey().getDeclaredConstructors()[0].newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("Test ran for " + entry.getKey());

        List<Method> listMethods = entry.getValue();
        List<Method> methodsBefore = getListMethodWithAnnotation(listMethods, Before.class);
        List<Method> methodsAfter = getListMethodWithAnnotation(listMethods, After.class);
        List<Method> methodsTest = getListMethodWithAnnotation(listMethods, Test.class);

        showError(invokedObject, entry.getKey().getName(), methodsBefore.size() == 1, methodsAfter.size() == 1);

        invokedTests(invokedObject, methodsTest, methodsBefore, methodsAfter);

    }

    private void invokedTests(Object invoked, List<Method> listTest, List<Method> listBefore, List<Method> listAfter) {

        ArrayList<Method> showAfterAssert = listTest.stream()
                .filter(method -> method.getAnnotation(Test.class).printTestResult().equals(PrintTestResult.AFTER_ASSERT))
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Method> showAfterMethod = listTest.stream()
                .filter(method -> method.getAnnotation(Test.class).printTestResult().equals(PrintTestResult.AFTER_METHOD))
                .collect(Collectors.toCollection(ArrayList::new));

        showAfterAssert(invoked, showAfterAssert, listBefore, listAfter);
        showAfterMethod(invoked, showAfterMethod, listBefore, listAfter);

    }

    private void showError(Object invokedObject, String name, boolean methodBefore, boolean methodAfter) {
        if (!(invokedObject instanceof IgnoreTestException)) {
            if (!methodBefore)
                throw new RuntimeException("More than one method with annotation @Before in class: " + name + "\n" + Arrays.toString(Thread.currentThread().getStackTrace()));
            if (!methodAfter)
                throw new RuntimeException("More than one method with annotation @After in class: " + name + "\n" + Arrays.toString(Thread.currentThread().getStackTrace()));
        }
    }

    private void showAfterAssert(Object invoked, List<Method> listAfterAssert, List<Method> listBefore, List<Method> listAfter) {
        listAfterAssert.forEach(method -> {
            try {
                testAssertCounter.setStatisticsLevel(method.getAnnotation(Test.class).statisticsTestLevel());
                testAssertCounter.setPrintLevel(method.getAnnotation(Test.class).printTestResult());
                runMethodList(invoked, listBefore);
                method.invoke(invoked);
                runMethodList(invoked, listAfter);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

    }

    private void showAfterMethod(Object invoked, List<Method> listAfterMethod, List<Method> listBefore, List<Method> listAfter) {
        listAfterMethod.forEach(method -> {
            try {
                testAssertCounter.init();
                testAssertCounter.setStatisticsLevel(method.getAnnotation(Test.class).statisticsTestLevel());
                testAssertCounter.setPrintLevel(method.getAnnotation(Test.class).printTestResult());
                runMethodList(invoked, listBefore);
                method.invoke(invoked);
                runMethodList(invoked, listAfter);
                System.out.println("Method name: " + method.getName());
                testAssertCounter.showResult();
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private void runMethodList(Object invoked, List<Method> list) {
        list.forEach(method -> {
            try {
                method.invoke(invoked);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}
