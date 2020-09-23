package tr.org.eaydemir.eventdriven.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import tr.org.eaydemir.eventdriven.exception.NoSuchProcessorDefinitionException;
import tr.org.eaydemir.eventdriven.listener.EventHandler;
import tr.org.eaydemir.eventdriven.process.EventProcessorExecutor;
import tr.org.eaydemir.eventdriven.publisher.EventPublisher;
import tr.org.eaydemir.eventdriven.publisher.GlobalEventPublisher;

import java.util.Map;
import java.util.concurrent.Executor;

@EnableAsync
@Configuration
@EnableConfigurationProperties({EventDrivenConfiguration.GlobalEventDrivenProperties.class})
public class EventDrivenConfiguration {

    private final GlobalEventDrivenProperties globalEventDrivenProperties;
    private final RestTemplate restTemplate;

    public EventDrivenConfiguration(GlobalEventDrivenProperties globalEventDrivenProperties) {
        this.globalEventDrivenProperties = globalEventDrivenProperties;
        this.restTemplate = new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventPublisher eventPublisher() {
        return new EventPublisher();
    }

    @Bean
    @DependsOn("eventProcessorExecutor")
    @ConditionalOnMissingBean
    public EventHandler eventHandler(EventProcessorExecutor eventProcessorExecutor) {
        return new EventHandler(eventProcessorExecutor);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventProcessorExecutor eventProcessorExecutor(ApplicationContext applicationContext) throws NoSuchProcessorDefinitionException {
        return new EventProcessorExecutor(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalEventPublisher globalEventPublisher() {
        Assert.notEmpty(globalEventDrivenProperties.getPublishers(), "Publishers cannot empty.");
        return new GlobalEventPublisher(globalEventDrivenProperties.getPublishers(), restTemplate);
    }

    @Bean
    @Qualifier("eventDrivenTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("EventDriven-Async");
        executor.initialize();
        return executor;
    }

    @Data
    @ConfigurationProperties(prefix = "global-event-driven")
    public static class GlobalEventDrivenProperties {

        /**
         * You need define handler service with url
         * Key -> service name
         * Value -> endpoint
         */
        Map<String, String> publishers;
    }

}
