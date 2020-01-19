package ws18.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Event implements Serializable {

    private EventType type;
    private Object object;

    public Event(EventType type, Object object) {
        this.object = object;
        this.type = type;

    }

}
