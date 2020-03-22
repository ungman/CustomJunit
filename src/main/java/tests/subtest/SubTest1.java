package tests.subtest;

import util.annotation.PrintTestResult;
import util.annotation.StatisticsTestLevel;
import util.annotation.Test;
import util.marker.IgnoreTestException;

import java.util.Date;

import static util.asserts.Assert.ownAssert;

public class SubTest1 implements IgnoreTestException {

    @Test(statisticsTestLevel = StatisticsTestLevel.SHOW_NAME_RESULT, printTestResult = PrintTestResult.AFTER_METHOD)
    public void firstTest() {
        ownAssert(false).isTrue();
        ownAssert().isNotNull();
        System.out.println("firstTest: SHOW_NAME_RESULT");
    }

    @Test(statisticsTestLevel = StatisticsTestLevel.ONLY_COUNTS)
    public void secondTest() {
        ownAssert("testString").isEquals(new Date());
        ownAssert(10).isEquals(10);
        System.out.println("secondTest: ONLY_COUNTS");
    }


    @Test(statisticsTestLevel = StatisticsTestLevel.NONE)
    public void thirdTest() {
        ownAssert(new Date()).isNotNull();
        ownAssert(true).isTrue();
        ownAssert(5).isEquals(5);
        System.out.println("thirdTest:  NONE");
    }

}
