package ws18.messagingutils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import gherkin.deps.com.google.gson.Gson;
import ws18.model.Event;

/**
 * 	Provided code from the instructor
 */
public class RabbitMqListener {

	IEventReceiver service;

	public RabbitMqListener(IEventReceiver service) {
		this.service = service;
	}

	public void listen() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitMQValues.HOST_NAME);
		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();
		channel.exchangeDeclare(RabbitMQValues.TOPIC_EXCHANGE_NAME, "topic");

		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, RabbitMQValues.TOPIC_EXCHANGE_NAME, RabbitMQValues.TOKEN_SERVICE_ROUTING_KEY);

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");

			System.out.println("[x] receiving "+message);

			Event event = new Gson().fromJson(message, Event.class);
			try {
				service.receiveEvent(event);
			} catch (Exception e) {
				throw new Error(e);
			}
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}
}
