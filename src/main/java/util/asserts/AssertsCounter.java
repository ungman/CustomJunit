package util.asserts;

import util.annotation.PrintTestResult;
import util.annotation.StatisticsTestLevel;

import java.util.Date;

public abstract class AssertsCounter {

    public abstract void init();

    public abstract void handleMethodResult(String nameMethod, String nameMethodAnn, Date date, Boolean result, Object... objects);

    public abstract void showResult();

    AssertsCounter() {
        statisticsLevel = StatisticsTestLevel.SHOW_NAME_RESULT;
        printLevel = PrintTestResult.AFTER_METHOD;
    }

    public StatisticsTestLevel getStatisticsLevel() {
        return statisticsLevel;
    }

    public void setStatisticsLevel(StatisticsTestLevel statisticsLevel) {
        this.statisticsLevel = statisticsLevel;
    }

    public PrintTestResult getPrintLevel() {
        return printLevel;
    }

    public void setPrintLevel(PrintTestResult printLevel) {
        this.printLevel = printLevel;
    }

    private StatisticsTestLevel statisticsLevel;
    private PrintTestResult printLevel;

}
