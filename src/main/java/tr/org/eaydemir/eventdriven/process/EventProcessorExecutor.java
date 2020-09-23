package tr.org.eaydemir.eventdriven.process;

import org.springframework.context.ApplicationContext;
import tr.org.eaydemir.eventdriven.exception.EventProcessorNotFoundException;
import tr.org.eaydemir.eventdriven.exception.NoSuchProcessorDefinitionException;
import tr.org.eaydemir.eventdriven.exception.ProcessInternalException;
import tr.org.eaydemir.eventdriven.model.Event;
import tr.org.eaydemir.eventdriven.process.annotation.Processor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class EventProcessorExecutor {

    /**
     * It will be filled with processor {@link EventProcessor} by constructor.
     */
    private final Map<String, EventProcessor> processorMap = new HashMap<>();

    /**
     * Get all {@link EventProcessor} classes. Mapped with eventName as key.
     * Searching for {@link Processor} annotation in Beans.
     *
     * @throws NoSuchProcessorDefinitionException
     */
    public EventProcessorExecutor(ApplicationContext applicationContext) throws NoSuchProcessorDefinitionException {
        final Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Processor.class);
        if (beansWithAnnotation.size() > 0) {
            beansWithAnnotation.forEach((s, o) -> {
                final Processor annotation = o.getClass().getDeclaredAnnotation(Processor.class);
                processorMap.put(annotation.eventName(), (EventProcessor) o);
            });
        } else {
            throw new NoSuchProcessorDefinitionException("New instance must be created. It should be implemented from EventProcessor and have Processor annotation..");
        }
    }

    /**
     * This method allows the event to be processed by selecting
     * the process that the event belongs to using a strategy design pattern.
     *
     * @param event will be precess event
     * @return processed event.
     * @throws EventProcessorNotFoundException
     * @throws ProcessInternalException
     */
    public CompletableFuture<Event> execute(Event event) throws EventProcessorNotFoundException, ProcessInternalException {
        final EventProcessor eventProcessor = processorMap.get(event.getEventName());
        if (eventProcessor == null) {
            throw new EventProcessorNotFoundException("Event processor not found for " + event.getEventName().toString());
        }
        return CompletableFuture.completedFuture(eventProcessor.process(event));
    }
}
