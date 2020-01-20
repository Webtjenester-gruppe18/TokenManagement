package ws18.messagingutils;

import ws18.model.Event;


public interface IEventSender {
    void sendEvent(Event event) throws Exception;
}
