package br.ifsp.edu.report.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_FEEDBACK = "feedback.exchange";

    public static final String QUEUE_REPORT_CREATED = "report.feedback.created.queue";
    public static final String QUEUE_REPORT_DELETED = "report.feedback.deleted.queue";

    public static final String ROUTING_KEY_FEEDBACK_CREATED = "feedback.created";
    public static final String ROUTING_KEY_FEEDBACK_DELETED = "feedback.deleted";

    @Bean
    TopicExchange feedbackExchange() {
        return new TopicExchange(EXCHANGE_FEEDBACK);
    }

    @Bean
    Queue reportCreatedQueue() {
        return new Queue(QUEUE_REPORT_CREATED, true);
    }

    @Bean
    Queue reportDeletedQueue() {
        return new Queue(QUEUE_REPORT_DELETED, true);
    }

    @Bean
    Binding bindingCreated() {
        return BindingBuilder.bind(reportCreatedQueue())
                .to(feedbackExchange())
                .with(ROUTING_KEY_FEEDBACK_CREATED);
    }

    @Bean
    Binding bindingDeleted() {
        return BindingBuilder.bind(reportDeletedQueue())
                .to(feedbackExchange())
                .with(ROUTING_KEY_FEEDBACK_DELETED);
    }
    
    @Bean
    MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
