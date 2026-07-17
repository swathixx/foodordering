package com.foodordering.orderservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@EnableJms
public class JmsConfig {

    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        java.util.Map<String, Class<?>> typeIdMappings = new java.util.HashMap<>();
        typeIdMappings.put("com.foodordering.orderservice.event.OrderCreatedEvent", com.foodordering.orderservice.event.OrderCreatedEvent.class);
        typeIdMappings.put("com.foodorder.payment.event.PaymentCompletedEvent", com.foodordering.orderservice.event.PaymentCompletedEvent.class);
        typeIdMappings.put("com.foodordering.orderservice.event.KitchenPendingEvent", com.foodordering.orderservice.event.KitchenPendingEvent.class);
        typeIdMappings.put("com.foodorder.kitchen.event.KitchenCompletedEvent", com.foodordering.orderservice.event.KitchenCompletedEvent.class);
        typeIdMappings.put("com.foodordering.orderservice.event.DeliveryPendingEvent", com.foodordering.orderservice.event.DeliveryPendingEvent.class);
        typeIdMappings.put("com.foodorder.delivery.event.DeliveryCompletedEvent", com.foodordering.orderservice.event.DeliveryCompletedEvent.class);
        converter.setTypeIdMappings(typeIdMappings);

        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
