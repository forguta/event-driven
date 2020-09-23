package tr.org.eaydemir.eventdriven.listener;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import tr.org.eaydemir.eventdriven.model.Event;
import tr.org.eaydemir.eventdriven.exception.EventProcessorNotFoundException;
import tr.org.eaydemir.eventdriven.exception.ProcessInternalException;
import tr.org.eaydemir.eventdriven.process.EventProcessorExecutor;

import java.util.concurrent.ExecutionException;

@Log4j2
@AllArgsConstructor
public class EventHandler {

    private final EventProcessorExecutor eventProcessorExecutor;

    @EventListener(condition = "!#event.async")
    public Event handleEvent(Event event) {
        try {
            log.info("Event handle -> name = {}, object = {}", event.getEventName(), event.getSource());
            event = eventProcessorExecutor.execute(event).get();
        } catch (EventProcessorNotFoundException | ProcessInternalException | InterruptedException | ExecutionException exception) {
            event.failed(exception);
            log.error(exception.getMessage(), exception);
        }
        return event;
    }

    @Async
    @EventListener(condition = "#event.async")
    public void asyncHandleEvent(Event event) {
        try {
            log.info("Async event handle -> name = {}, object = {}", event.getEventName(), event.getSource());
            eventProcessorExecutor.execute(event);
        } catch (EventProcessorNotFoundException | ProcessInternalException exception) {
            event.failed(exception);
            log.error(exception.getMessage(), exception);
        }
    }


}
