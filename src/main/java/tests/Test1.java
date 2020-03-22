package tests;

import util.annotation.*;

import static util.asserts.Assert.ownAssert;


public class Test1 {

    String name = "Test";
    String nullString;
    String nullStringInitBefore;
    int value10 = 10;
    int value15 = 10;
    boolean isTrue = true;
    boolean isFalse = false;

    @Before
    public void init() {
        System.out.println("Show that @before calling 'before' @Test method ");
        nullStringInitBefore = "Im not empty ";
        value15 = 15;
    }

    @After
    public void close() {
        System.out.println("Show that @after calling 'after' @Test method ");
    }

    @Test(statisticsTestLevel = StatisticsTestLevel.ALL, printTestResult = PrintTestResult.AFTER_ASSERT)
    public void test() {
        ownAssert(name).isNotNull();
        ownAssert(nullString).isNotNull();

        ownAssert(value10).isEquals(10);
        ownAssert(value10).isEquals(value15);

        ownAssert(value10 == value15).isTrue();
        ownAssert(nullStringInitBefore.equals(name));
        ownAssert(isTrue).isTrue();
        ownAssert(isFalse).isTrue();
    }

}
