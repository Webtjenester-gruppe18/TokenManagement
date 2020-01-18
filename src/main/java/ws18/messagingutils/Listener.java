package ws18.messagingutils;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ws18.model.Event;
import ws18.model.EventType;
import ws18.service.ITokenManager;

@Service
public class Listener {
    private RabbitTemplate rabbitTemplate;
    private ITokenManager tokenManager;

    @Autowired
    public Listener(RabbitTemplate rabbitTemplate, ITokenManager tokenManager) {
        this.rabbitTemplate = rabbitTemplate;
        this.tokenManager = tokenManager;
    }

    @RabbitListener(queues = {RabbitMQValues.TOKEN_SERVICE_QUEUE_NAME})
    public void receiveEventFromTokenQueue(Event event) {
        System.out.println("Token manager received message: " + event.getType());
        if (event.getType().equals(EventType.TOKEN_VALIDATION_REQUEST)) {
            this.rabbitTemplate.convertAndSend(RabbitMQValues.TOPIC_EXCHANGE_NAME, RabbitMQValues.PAYMENT_SERVICE_ROUTING_KEY, event);

        }
    }
}
