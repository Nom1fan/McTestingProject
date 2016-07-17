package framework.tests;

import framework.infra.ObjectFactory;
import framework.infra.config.Config;
import framework.infra.config.JavaConfig;
import framework.infra.network.ServerProxy;
import framework.infra.tests.TestCase;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Created by Mor on 14/07/2016.
 */
public class Main {

    private static Reflections reflections = new Reflections();

    public static void main(String args[]) {
        if(args.length==0)
            runAllTests();
        else
            runTests(args);
    }

    private static void runTests(String[] args) {
        for (String arg : args) {
            try {
                Class<TestCase> aClass = (Class<TestCase>) Class.forName("framework.tests."+arg);
                runTest(aClass);
            } catch(Exception e) {
                e.printStackTrace();
                System.out.println("Test case:" + arg +" failed:" + e.getMessage());
            }

        }
    }

    private static void runAllTests()  {
        Set<Class<? extends TestCase>> classes = reflections.getSubTypesOf(TestCase.class);
        classes.forEach(Main::runTest);
    }

    private static void runTest(Class<? extends TestCase> aClass) {
        try {
            TestCase tc = ObjectFactory.getInstance().createObject(aClass);
            tc.runTestFlow();
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Test case:" + aClass.getSimpleName() +" failed");
        }
    }


}
