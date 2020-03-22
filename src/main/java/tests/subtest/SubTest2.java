package tests.subtest;

import util.annotation.After;
import util.annotation.Before;
import util.annotation.Test;

public class SubTest2 {
    @Before
    public void before() {
        System.out.println("    Run before");
    }

    @After
    public void after() {
        System.out.println("    Run after");
    }

    @Test
    public void first() {
        System.out.println("    Run first");
    }

    @Test
    public void second() {
        System.out.println("    Run second");
    }
}
