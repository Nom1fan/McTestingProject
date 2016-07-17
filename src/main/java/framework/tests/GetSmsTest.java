package framework.tests;

import framework.infra.exceptions.TestFailedException;
import EventObjects.EventType;
import framework.infra.tests.TestCase;

import java.io.IOException;

/**
 * Created by Mor on 14/07/2016.
 */
public class GetSmsTest extends TestCase {


    @Override
    protected void runTest() throws InterruptedException, TestFailedException, IOException {
        _serverProxy.prepareSmsCode(_userA.getId());
        assertResult(EventType.GET_SMS_CODE_SUCCESS);
    }

    @Override
    protected void testCleanUp() {
    }
}
