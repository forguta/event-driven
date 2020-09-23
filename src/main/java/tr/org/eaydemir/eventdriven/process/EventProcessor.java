package tr.org.eaydemir.eventdriven.process;

import tr.org.eaydemir.eventdriven.exception.ProcessInternalException;
import tr.org.eaydemir.eventdriven.model.Event;

public interface EventProcessor {

    Event process(Event event) throws ProcessInternalException;
}
