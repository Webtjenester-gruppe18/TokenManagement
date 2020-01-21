package ws18.messagingutils;

import ws18.model.Event;

/**
 * @author Emil Vinkel, s175107
 */

public interface IEventSender {
    void sendEvent(Event event) throws Exception;
}
