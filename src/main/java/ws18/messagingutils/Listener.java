package ws18.messagingutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ws18.exceptions.TokenValidationException;
import ws18.exceptions.TooManyTokensException;
import ws18.model.Event;
import ws18.model.EventType;
import ws18.model.PaymentRequest;
import ws18.model.Token;
import ws18.service.ITokenManager;

import java.util.ArrayList;

@Service
public class Listener {
    private RabbitTemplate rabbitTemplate;
    private ITokenManager tokenManager;
    private ObjectMapper mapper;

    @Autowired
    public Listener(RabbitTemplate rabbitTemplate, ITokenManager tokenManager, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.tokenManager = tokenManager;
        this.mapper = objectMapper;
    }

    @RabbitListener(queues = {RabbitMQValues.TOKEN_SERVICE_QUEUE_NAME})
    public void receiveEventFromTokenQueue(Event event) {
        System.out.println("Token manager received message: " + event.getType());

        if (event.getType().equals(EventType.TOKEN_VALIDATION_REQUEST)) {
            PaymentRequest paymentRequest = mapper.convertValue(event.getObject(), PaymentRequest.class);

            try {
                this.tokenManager.validateToken(paymentRequest.getCpr(), paymentRequest.getToken());
            } catch (TokenValidationException e) {
                Event response = new Event(EventType.TOKEN_VALIDATION_FAILED, e);
                this.rabbitTemplate.convertAndSend(RabbitMQValues.TOPIC_EXCHANGE_NAME, RabbitMQValues.DTU_SERVICE_ROUTING_KEY, response);
            }

            event.setType(EventType.MONEY_TRANSFER_REQUEST);
            this.rabbitTemplate.convertAndSend(RabbitMQValues.TOPIC_EXCHANGE_NAME, RabbitMQValues.PAYMENT_SERVICE_ROUTING_KEY, event);
        } else if (event.getType().equals(EventType.REQUEST_FOR_NEW_TOKENS)) {
            String cpr = mapper.convertValue(event.getObject(), String.class);

            try {
                this.tokenManager.requestForNewTokens(cpr);
            } catch (TooManyTokensException e) {
                Event response = new Event(EventType.TOKEN_GENERATION_FAILED, e);
                this.rabbitTemplate.convertAndSend(RabbitMQValues.TOPIC_EXCHANGE_NAME, RabbitMQValues.DTU_SERVICE_ROUTING_KEY, response);
                return;
            }

            Event successResponse = new Event(EventType.TOKEN_GENERATION_SUCCEED, EventType.TOKEN_GENERATION_SUCCEED);
            this.rabbitTemplate.convertAndSend(RabbitMQValues.TOPIC_EXCHANGE_NAME, RabbitMQValues.DTU_SERVICE_ROUTING_KEY, successResponse);

        } else if (event.getType().equals(EventType.RETRIEVE_TOKENS)) {
            String cpr = mapper.convertValue(event.getObject(), String.class);
            ArrayList<Token> tokens = this.tokenManager.getUnusedTokensByCpr(cpr);
            if (tokens.isEmpty()) {
                Event successResponse = new Event(EventType.RETRIEVE_TOKENS_FAILED, tokens);
                this.rabbitTemplate.convertAndSend(RabbitMQValues.TOPIC_EXCHANGE_NAME, RabbitMQValues.DTU_SERVICE_ROUTING_KEY, successResponse);
            } else {
                Event successResponse = new Event(EventType.RETRIEVE_TOKENS_SUCCEED, tokens);
                this.rabbitTemplate.convertAndSend(RabbitMQValues.TOPIC_EXCHANGE_NAME, RabbitMQValues.DTU_SERVICE_ROUTING_KEY, successResponse);
            }

        }
    }
}
