package tr.org.eaydemir.eventdriven.publisher;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.RestTemplate;
import tr.org.eaydemir.eventdriven.model.Event;

import java.util.Map;

@Log4j2
@AllArgsConstructor
public class GlobalEventPublisher {

    private final Map<String, String> serviceRequestUrlMap;
    private final RestTemplate restTemplate;

    public Event publishEvent(String serviceName, Event event) {
        return restTemplate.postForObject(this.serviceRequestUrlMap.get(serviceName).concat("/event/" + event.getEventName()), event, Event.class);
    }
}
