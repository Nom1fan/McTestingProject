package framework.infra.tests;

/**
 * Created by Mor on 15/07/2016.
 */
public class TestResult {
    public enum Result {
        PASSED,
        FAILED,

    }

    private Result result;
    private Exception exception;

    public TestResult(Result result) {
        this.result = result;
    }

    public TestResult(Result result, Exception exception) {
        this.exception = exception;
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }
}
