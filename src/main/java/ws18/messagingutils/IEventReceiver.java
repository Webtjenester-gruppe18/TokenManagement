package ws18.messagingutils;

import ws18.model.Event;

/**
 * @author Emil Vinkel, s175107
 */

public interface IEventReceiver {
    void receiveEvent(Event event) throws Exception;
}
