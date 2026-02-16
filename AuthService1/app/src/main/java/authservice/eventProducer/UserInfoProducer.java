package authservice.eventProducer;


import authservice.model.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoProducer {

    private final KafkaTemplate<String, UserInfoDto> kafkaTemplate; // topic

    @Value("${spring.kafka.topic-json.name}")
    private String TOPIC_NAME;


    UserInfoProducer(KafkaTemplate<String,UserInfoDto> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventToKafka(UserInfoEvent userInfoDto){
        Message<UserInfoEvent > message = MessageBuilder
                .withPayload(userInfoDto)
                .setHeader(KafkaHeaders.TOPIC,TOPIC_NAME)
                .build();

        kafkaTemplate.send(message);
    }


}
