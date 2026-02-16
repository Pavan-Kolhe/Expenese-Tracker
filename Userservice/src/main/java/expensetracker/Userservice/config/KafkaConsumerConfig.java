package expensetracker.Userservice.config;

import expensetracker.Userservice.deserializer.UserInfoDeserializer;
import expensetracker.Userservice.entities.UserInfoDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, UserInfoDto> consumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserInfoDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 45000);
        config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public CommonErrorHandler errorHandler() {
        return new DefaultErrorHandler();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserInfoDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserInfoDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(false);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.setCommonErrorHandler(errorHandler());

        return factory;
    }
}


//package expensetracker.Userservice.config;
//
//import expensetracker.Userservice.deserializer.UserInfoDeserializer;
//import expensetracker.Userservice.entities.UserInfoDto;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.listener.ContainerProperties;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaConsumerConfig {
//
//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapServers;
//
//    @Value("${spring.kafka.consumer.group-id}")
//    private String groupId;
//
//    @Bean
//    public ConsumerFactory<String, UserInfoDto> consumerFactory() {
//        Map<String, Object> config = new HashMap<>();
//
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
//        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserInfoDeserializer.class);
//        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
//        config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 45000);
//        config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
//
//        return new DefaultKafkaConsumerFactory<>(config);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, UserInfoDto> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, UserInfoDto> factory =
//            new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//
//        // Enable batch listening if needed
//        factory.setBatchListener(false);
//
//        // Add logging for acknowledgment
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
//
//        return factory;
//    }
//}