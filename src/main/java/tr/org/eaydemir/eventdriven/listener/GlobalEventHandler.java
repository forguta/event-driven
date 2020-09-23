package tr.org.eaydemir.eventdriven.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tr.org.eaydemir.eventdriven.model.Event;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Log4j2
@RestController
public class GlobalEventHandler {

    private static final Map<String, Function<Event, Event>> eventProcessBehaviourMap = new HashMap<>();

    public GlobalEventHandler(EventHandler eventHandler) {
        eventProcessBehaviourMap.put("async", (event) -> {
            eventHandler.asyncHandleEvent(event);
            return event;
        });
        eventProcessBehaviourMap.put("sync", eventHandler::handleEvent);
    }

    @PostMapping("/event/{eventName}")
    public Event globalEventHandler(@PathVariable String eventName, @RequestBody Event event) {
        log.info("received: {}, {} at {}", eventName, event, Instant.now());
        if (event.isAsync()) {
            eventProcessBehaviourMap.get("async").apply(event);
            return null;
        }
        return eventProcessBehaviourMap.get("sync").apply(event);
    }

}
