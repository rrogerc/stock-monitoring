package com.rrogerc.stock_monitor.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "b-2-public.stockmonitorcluster.o82tll.c2.kafka.ca-central-1.amazonaws.com:9198," +
                        "b-3-public.stockmonitorcluster.o82tll.c2.kafka.ca-central-1.amazonaws.com:9198," +
                        "b-1-public.stockmonitorcluster.o82tll.c2.kafka.ca-central-1.amazonaws.com:9198");

        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // AWS MSK IAM Authentication
        config.put("security.protocol", "SASL_SSL");
        config.put("sasl.mechanism", "AWS_MSK_IAM");
        config.put("sasl.jaas.config", "software.amazon.msk.auth.iam.IAMLoginModule required;");
        config.put("sasl.client.callback.handler.class", "software.amazon.msk.auth.iam.IAMClientCallbackHandler");
        config.put("client.dns.lookup", "use_all_dns_ips");

        // Use AWS environment variables or default profile
        config.put("aws.access.key.id", System.getenv("AWS_ACCESS_KEY_ID"));
        config.put("aws.secret.key.id", System.getenv("AWS_SECRET_ACCESS_KEY"));
        config.put("aws.region", System.getenv("AWS_REGION"));

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
