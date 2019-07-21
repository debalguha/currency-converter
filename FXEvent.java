import java.util.EventObject;

public class FXEvent extends EventObject {

    public  enum EventType {ENTRY_UPDATE}
    private final FXEntry entry;
    private final EventType eventType;
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public FXEvent(Object source, FXEntry entry, EventType eventType) {
        super(source);
        this.entry = entry;
        this.eventType = eventType;
    }

    public FXEntry getNewEntry() {
        return entry;
    }

    public EventType getEventType() {
        return eventType;
    }
}
