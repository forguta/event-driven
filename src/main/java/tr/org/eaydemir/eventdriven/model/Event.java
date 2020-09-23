package tr.org.eaydemir.eventdriven.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class Event extends ApplicationEvent {

    private String id;
    private String parentId;

    private String eventName;
    private String relatedId;

    private boolean async;
    private boolean success;
    private String failedException;
    private String message;

    @Builder
    public Event(String eventName, Object source, String parentId, boolean async) {
        super(source);
        this.eventName = eventName;
        this.id = UUID.randomUUID().toString();
        this.parentId = parentId;
        this.async = async;
    }

    public Event succeeded() {
        this.success = true;
        return this;
    }

    public Event succeeded(String message) {
        this.success = true;
        this.message = message;
        return this;
    }

    public Event failed(Exception failedException) {
        this.failedException = failedException.getClass().getSimpleName();
        this.success = false;
        return this;
    }

    public Event failed(String message, Exception failedException) {
        this.success = false;
        this.failedException = failedException.getClass().getSimpleName();
        this.message = message;
        return this;
    }

}
