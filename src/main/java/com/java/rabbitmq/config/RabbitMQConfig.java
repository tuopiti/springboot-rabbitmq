package com.java.rabbitmq.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import org.springframework.messaging.converter.MessageConverter;


@Configuration
public class RabbitMQConfig {
//    public static final String QUEUE = "my_queue";
//    public static final String EXCHANGE = "my_exchange";
//    public static final String ROUTING_KEY = "my_routing_key";

    @Value("${rabbitmq.queue.name}")
    private String queue;
    @Value("${rabbitmq.queue.json.name}")
    private String jsonQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.name}")
    private String routingKey;
    @Value("${rabbitmq.routing.json.name}")
    private String routingJsonKey;

    // spring bean for rabbitmq queue
    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    // spring bean for queue (store json message)
    @Bean
    public Queue jsonQueue(){
        return new Queue(jsonQueue);
    }

    //spring bean for rabbitmq exchange
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

//    public TopicExchange exchange(){
//        return new TopicExchange(EXCHANGE)
//    }

    //binding between queue and exchange using routing key
    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(jsonQueue())
                .to(exchange())
                .with(routingKey);
    }

    //binding between queue and exchange using routing key
    @Bean
    public Binding jsonBinding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingJsonKey);
    }

    @Bean
    public MessageConverter converter(){
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate((org.springframework.amqp.rabbit.connection.ConnectionFactory) connectionFactory);
        rabbitTemplate.setMessageConverter((org.springframework.amqp.support.converter.MessageConverter) converter());
        return rabbitTemplate;
    }
}
