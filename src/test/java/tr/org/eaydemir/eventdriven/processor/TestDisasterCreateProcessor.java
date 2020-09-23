package tr.org.eaydemir.eventdriven.processor;

import tr.org.eaydemir.eventdriven.exception.ProcessInternalException;
import tr.org.eaydemir.eventdriven.model.Event;
import tr.org.eaydemir.eventdriven.process.EventProcessor;
import tr.org.eaydemir.eventdriven.process.annotation.Processor;

@Processor(eventName = "DISASTER_TEST")
public class TestDisasterCreateProcessor implements EventProcessor {

    @Override
    public Event process(Event event) throws ProcessInternalException {
        return event.succeeded();
    }
}