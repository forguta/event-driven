package tr.org.eaydemir.eventdriven.exception;

public class EventProcessorNotFoundException extends Exception {

    public EventProcessorNotFoundException() {
    }

    public EventProcessorNotFoundException(String message) {
        super(message);
    }
}
