package ws18.messagingutils;

import ws18.model.Event;

public interface IEventReceiver {
    void receiveEvent(Event event) throws Exception;
}
