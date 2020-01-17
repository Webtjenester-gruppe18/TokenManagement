package ws18;

import configuration.RabbitMQValues;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class TokenManagementApplication {
    private static final String queueName = "token-queue";
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public TokenManagementApplication(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }



    @RabbitListener(queues = {queueName})
    public void receiveMessageFromFanout1(String message) {
        System.out.println("Token manager received message: " + message);
        this.rabbitTemplate.convertAndSend(RabbitMQValues.topicExchangeName, "response", "Completed");
    }
    public static void main(String[] args) {
        SpringApplication.run(TokenManagementApplication.class, args);
    }
}

