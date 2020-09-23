package tr.org.eaydemir.eventdriven.exception;

public class NoSuchProcessorDefinitionException extends Exception {

    public NoSuchProcessorDefinitionException() {
    }

    public NoSuchProcessorDefinitionException(String message) {
        super(message);
    }
}
