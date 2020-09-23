package tr.org.eaydemir.eventdriven;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import tr.org.eaydemir.eventdriven.configuration.EventDrivenConfiguration;
import tr.org.eaydemir.eventdriven.model.Event;
import tr.org.eaydemir.eventdriven.processor.TestDisasterCreateProcessor;
import tr.org.eaydemir.eventdriven.publisher.EventPublisher;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Log4j2
@SpringBootTest(classes = {EventDrivenConfiguration.class, TestDisasterCreateProcessor.class})
public class EventDrivenApplicationTests {

    @Autowired
    private EventPublisher eventPublisher;

    private static final CountDownLatch lock = new CountDownLatch(1);

    @Test
    public void contextLoader() throws InterruptedException {
        lock.await(5000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testSyncAnnouncing() {
        final Event event = Event.builder()
                .eventName("DISASTER_TEST")
                .async(false)
                .source(new Object())
                .build();
        eventPublisher.publishEvent(event);
        Assert.isTrue(event.isSuccess(), "Event cannot be processed as a succeeded.");
    }

    @Test
    public void testAsyncAnnouncing() throws InterruptedException {
        final Event event = Event.builder()
                .eventName("DISASTER_TEST")
                .async(true)
                .source(new Object())
                .build();
        eventPublisher.publishEvent(event);
        lock.await(2000, TimeUnit.MILLISECONDS);
        Assert.isTrue(event.isSuccess(), "Event cannot be processed as a succeeded.");
    }

    @Test
    public void testSyncAnnouncing_shouldTThrowEventProcessorNotFoundException() {
        final Event event = Event.builder()
                .eventName("DISASTER_TEST1")
                .async(false)
                .source(new Object())
                .build();
        eventPublisher.publishEvent(event);
        Assert.isTrue("EventProcessorNotFoundException".equals(event.getFailedException()), "Should throw exception.");
    }

    @Test
    public void testAsyncAnnouncing_shouldThrowEventProcessorNotFoundException() throws InterruptedException {
        final Event event = Event.builder()
                .eventName("DISASTER_TEST1")
                .async(true)
                .source(new Object())
                .build();
        eventPublisher.publishEvent(event);
        lock.await(2000, TimeUnit.MILLISECONDS);
        Assert.isTrue("EventProcessorNotFoundException".equals(event.getFailedException()), "Should throw exception.");

    }


}
