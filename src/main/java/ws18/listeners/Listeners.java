package ws18.listeners;

import configuration.RabbitMQValues;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ws18.exceptions.TooManyTokensException;
import ws18.service.ITokenManager;

@Service
public class Listeners {

    private static final String queueName = "token-queue";
    private RabbitTemplate rabbitTemplate;
    private ITokenManager tokenManager;

    @Autowired
    public Listeners(RabbitTemplate rabbitTemplate, ITokenManager tokenManager){
        this.rabbitTemplate = rabbitTemplate;
        this.tokenManager = tokenManager;
    }

    @RabbitListener(queues = {queueName})
    public void receiveEventFromTokenQueue(String message) {
        System.out.println("Token manager received message: " + message);

        this.rabbitTemplate.convertAndSend(RabbitMQValues.topicExchangeName, "response", "Completed");
    }
}
