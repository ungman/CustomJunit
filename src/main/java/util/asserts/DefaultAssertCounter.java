package util.asserts;


import util.annotation.PrintTestResult;
import util.annotation.StatisticsTestLevel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiConsumer;

public class DefaultAssertCounter extends AssertsCounter {

    private static DefaultAssertCounter instance;
    private LinkedHashMap<String, HashMap<String, HashMap<Date, List<Object>>>> methodsTestCounter = new LinkedHashMap<>();
    private int countPassedTest = 0;
    private int countFailedTest = 0;

    private DefaultAssertCounter() {
        super();
        init();
    }

    public static DefaultAssertCounter getInstance() {
        if (instance == null)
            instance = new DefaultAssertCounter();
        return instance;
    }

    public void init() {
        methodsTestCounter.clear();
        countFailedTest = 0;
        countPassedTest = 0;
    }

    public void handleMethodResult(String nameMethod, String nameMethodAnn, Date date, Boolean result, Object... objects) {
        if (getPrintLevel() == PrintTestResult.AFTER_ASSERT)
            init();

        date = new Date(date.getTime() + (countPassedTest + countFailedTest));//need for HashMap key (another date calling methodAnnotations is equals)
        ArrayList<Object> objectArrayList = new ArrayList<Object>() {{
            add(result);
            addAll(Arrays.asList(objects));
        }};

        putResultToMap(nameMethod, nameMethodAnn, date, objectArrayList);

        if (result)
            countPassedTest++;
        else
            countFailedTest++;

        if (getPrintLevel() == PrintTestResult.AFTER_ASSERT) {
            showResult();
        }

    }

    private void putResultToMap(String nameMethod, String nameMethodAnn, Date finalDate, ArrayList objectArrayList) {
        if (methodsTestCounter.get(nameMethod) == null) {
            methodsTestCounter.put(nameMethod, new HashMap<String, HashMap<Date, List<Object>>>() {{
                put(nameMethodAnn, new HashMap<Date, List<Object>>() {{
                    put(finalDate, objectArrayList);
                }});
            }});
        } else if (methodsTestCounter.get(nameMethod).get(nameMethodAnn) == null) {
            methodsTestCounter.get(nameMethod)
                    .put(nameMethodAnn, new HashMap<Date, List<Object>>() {{
                        put(finalDate, objectArrayList);
                    }});
        } else {
            methodsTestCounter.get(nameMethod).get(nameMethodAnn).put(finalDate, objectArrayList);
        }


    }

    public void showResult() {

        BiConsumer<Date, List<Object>> printLevel = getStatisticsLevelConsumer(getStatisticsLevel());

        if (printLevel == null) {
            System.out.println();
            return;
        }

        if (methodsTestCounter != null) {
            for (Map.Entry<String, HashMap<String, HashMap<Date, List<Object>>>> entry : methodsTestCounter.entrySet()) {
                entry.getValue().forEach(((nameAssert, dateBooleanHashMap) -> {
                    System.out.println("  Name asserts: " + nameAssert);
                    dateBooleanHashMap
                            .forEach(printLevel);
                }));
            }

            System.out.println("Test " + (countFailedTest + countPassedTest) + "; Passed: " + countPassedTest + "; Failed: " + countFailedTest);
            System.out.println();


        }
    }

    private BiConsumer<Date, List<Object>> getStatisticsLevelConsumer(StatisticsTestLevel levelPrint) {

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:ms");
        BiConsumer<Date, List<Object>> printLevel = null;

        if (levelPrint.equals(StatisticsTestLevel.NONE))
            printLevel = null;

        if (levelPrint.equals(StatisticsTestLevel.ONLY_COUNTS))
            printLevel = (k, v) -> {
            };

        if (levelPrint.equals(StatisticsTestLevel.SHOW_NAME_RESULT))
            printLevel = (date, result) -> System.out.println("     " + dateFormat.format(date) + "; Expected:" + result.get(0));

        if (levelPrint.equals(StatisticsTestLevel.ALL))
            printLevel = (date, result) -> {
                System.out.println("     " + dateFormat.format(date) + "; Expected:" + result.get(0));
                System.out.print("     data: ");
                result.stream().skip(1).forEach(data -> System.out.print(data + ";   "));
                System.out.println();
            };

        return printLevel;
    }
}
