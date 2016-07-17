package framework.tests;

import EventObjects.EventType;
import framework.infra.exceptions.TestFailedException;
import framework.infra.tests.TestCase;

import java.io.IOException;

/**
 * Created by Mor on 14/07/2016.
 */
public class PingTest extends TestCase {


    @Override
    protected void runTest() throws InterruptedException, TestFailedException, IOException {
        _serverProxy.ping(_userA.getId());
        long start = System.currentTimeMillis();
        assertResult(EventType.PING);
        long end = System.currentTimeMillis();
        _logger.info("Time until pong:" + (end-start));
    }

    @Override
    protected void testCleanUp() {
    }
}
