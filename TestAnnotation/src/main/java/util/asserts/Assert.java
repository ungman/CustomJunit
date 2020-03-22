package util.asserts;

import java.util.Date;

public class Assert {

    public static Object object;
    public static Assert instanceAssert;
    private static final AssertsCounter testAssertCounters = DefaultAssertCounter.getInstance();
    private String nameMethod;
    private String nameMethodAnnotations;

    private Assert() {
    }

    public static Assert ownAssert(Object... object1) {

        if (object1 == null || object1.length < 1) {
            object = null;
        } else {
            object = object1[0];
        }

        if (instanceAssert == null) {
            instanceAssert = new Assert();
        }
        instanceAssert.nameMethod = Thread.currentThread().getStackTrace()[2].getMethodName();
        return instanceAssert;
    }

    public void isTrue() {
        nameMethodAnnotations = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
            Boolean cast = (Boolean) object;
            if (cast) {
                testAssertCounters.handleMethodResult(nameMethod, nameMethodAnnotations, new Date(), true, object);
            } else {
                testAssertCounters.handleMethodResult(nameMethod, nameMethodAnnotations, new Date(), false, object);
            }
        } catch (Exception e) {
            testAssertCounters.handleMethodResult(nameMethod, nameMethodAnnotations, new Date(), false, object);
        }
        //notify?
    }

    public void isEquals(Object object1) {
        nameMethodAnnotations = Thread.currentThread().getStackTrace()[1].getMethodName();
        if (object.equals(object1)) {
            testAssertCounters.handleMethodResult(nameMethod, nameMethodAnnotations, new Date(), true, object, object1);
        } else {
            testAssertCounters.handleMethodResult(nameMethod, nameMethodAnnotations, new Date(), false, object, object1);
        }
    }

    public void isNotNull() {
        nameMethodAnnotations = Thread.currentThread().getStackTrace()[1].getMethodName();
        if (object != null)
            testAssertCounters.handleMethodResult(nameMethod, nameMethodAnnotations, new Date(), true, object);
        else
            testAssertCounters.handleMethodResult(nameMethod, nameMethodAnnotations, new Date(), false, object);
    }


}
