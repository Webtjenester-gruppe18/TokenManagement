package ws18;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import ws18.messagingutils.IEventSender;
import ws18.messagingutils.RabbitMqListener;
import ws18.messagingutils.RabbitMqSender;
import ws18.service.TokenManager;

@EnableSwagger2
@SpringBootApplication
public class TokenManagementApplication {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(TokenManagementApplication.class, args);
        new TokenManagementApplication().startUp();
    }
    private void startUp() throws Exception {
        IEventSender eventSender = new RabbitMqSender();
        TokenManager tokenManager = new TokenManager(eventSender);
        new RabbitMqListener(tokenManager).listen();
    }
}

