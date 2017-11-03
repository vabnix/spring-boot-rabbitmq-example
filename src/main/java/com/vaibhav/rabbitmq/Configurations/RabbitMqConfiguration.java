package com.vaibhav.rabbitmq.Configurations;

import com.vaibhav.rabbitmq.Receiver.Receiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    @Value("${spring.rabbitmq.host}")
    public String host;

    @Value("${spring.rabbitmq.queueName}")
    public String queueName;

    @Value("${spring.rabbitmq.username}")
    public String username;

    @Value("${spring.rabbitmq.password}")
    public String password;

    @Value("${spring.rabbitmq.port}")
    public String port;

    @Value("${spring.rabbitmq.exchange}")
    public String exchange;

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange(){
        return new TopicExchange("spring-boot-exchange");
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter adapter(Receiver receiver){
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
