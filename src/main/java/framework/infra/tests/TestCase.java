package framework.infra.tests;

import framework.infra.annotations.InjectByType;
import framework.infra.annotations.InjectLogger;
import framework.infra.data.TestUser;
import framework.infra.exceptions.TestFailedException;
import framework.infra.network.ServerProxy;
import EventObjects.EventReport;
import EventObjects.EventType;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Mor on 15/07/2016.
 */
public abstract class TestCase {

    private EventType _nextExpectedEvent;

    @InjectLogger(name = "MediaCallzTests", folder = "TestLogs")
    protected Logger _logger;

    @InjectByType
    protected TestUser _userA;

    @InjectByType
    protected ServerProxy _serverProxy;

    public TestResult runTestFlow() {

        try {
            _logger.info("Starting test run:" + this.getClass().getSimpleName());
            long start = System.currentTimeMillis();
            runTest();
            long end = System.currentTimeMillis();
            _logger.info("Finished test run. It took:" + (end - start) + " milliseconds");

        } catch (InterruptedException | IOException e) {
            String errMsg = "Error occured:"
                    + (e.getMessage() != null ? e.getMessage() : e);
            _logger.severe(errMsg);
            return new TestResult(TestResult.Result.FAILED, e);
        } catch (TestFailedException e) {
            String errMsg = (e.getMessage() != null ? e.getMessage() : e
                    .toString());
            _logger.severe(errMsg);
            return new TestResult(TestResult.Result.FAILED, e);
        } catch (Exception e) {
            String errMsg = (e.getMessage() != null ? e.getMessage() : e
                    .toString());
            _logger.severe(errMsg);
            return new TestResult(TestResult.Result.FAILED, e);
        } finally {
            _testCleanUp();
        }

        return new TestResult(TestResult.Result.PASSED);
    }

    /**
     * Test body to be implemented by tests
     *
     * @throws InterruptedException
     * @throws TestFailedException
     * @throws IOException
     */
    protected abstract void runTest() throws InterruptedException,
            TestFailedException, IOException;

    /**
     * Test cleanup to be implemented by tests
     */
    protected abstract void testCleanUp();

    /**
     * Asserting the result received from server.
     *
     * @param expectedResult The result expected to be received from server.
     * @throws TestFailedException  Thrown in case the result received is not as expected.
     * @throws InterruptedException Thrown if thread was interrupted while waiting for result.
     */
    protected void assertResult(EventType expectedResult)
            throws TestFailedException, InterruptedException {

        _nextExpectedEvent = expectedResult;
        EventReport actualResult = _serverProxy.getEventReport();

        TestFailedException e = null;
        if (actualResult == null)
            e = new TestFailedException("Test failed. [Expected result]:"
                    + _nextExpectedEvent + ". [Actual result]: null");
        if (actualResult != null && !actualResult.status().equals(_nextExpectedEvent))
            e = new TestFailedException("Test failed. [Expected result]:"
                    + _nextExpectedEvent + ". [Actual result]:"
                    + actualResult.status());
        if (e != null)
            throw e;
    }

    /**
     * Waits for result from server without asserting the result. Should be used
     * when performing cleanUp actions.
     *
     * @throws InterruptedException Thrown if thread was interrupted while waiting for result
     */
    protected void waitWithoutAssert() throws InterruptedException {

        _serverProxy.getEventReport();
    }

    /**
     * Wrapper method called when the test is finished. Invoking
     * test-implemented cleanUp.
     */
    private void _testCleanUp() {

        _logger.info("Performing cleanUp...");
        testCleanUp();
        _logger.info("CleanUp complete.");
    }

}

