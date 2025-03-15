package com.rrogerc.stock_monitor.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "b-2-public.stockmonitorcluster.o82tll.c2.kafka.ca-central-1.amazonaws.com:9198," +
                        "b-3-public.stockmonitorcluster.o82tll.c2.kafka.ca-central-1.amazonaws.com:9198," +
                        "b-1-public.stockmonitorcluster.o82tll.c2.kafka.ca-central-1.amazonaws.com:9198");

        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        config.put("security.protocol", "SASL_SSL");
        config.put("sasl.mechanism", "AWS_MSK_IAM");
        config.put("sasl.jaas.config", "software.amazon.msk.auth.iam.IAMLoginModule required;");
        config.put("sasl.client.callback.handler.class", "software.amazon.msk.auth.iam.IAMClientCallbackHandler");

        // Use AWS environment variables or default profile
        config.put("aws.access.key.id", System.getenv("AWS_ACCESS_KEY_ID"));
        config.put("aws.secret.key.id", System.getenv("AWS_SECRET_ACCESS_KEY"));
        config.put("aws.region", System.getenv("AWS_REGION"));

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}


