package lab6.excepcions;

public class InvalidConnectException extends MyException {

    public InvalidConnectException(String errorDescription) {
        super("проблемы с соединением", errorDescription);
    }
}
