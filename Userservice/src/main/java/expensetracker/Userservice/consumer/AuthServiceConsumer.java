package expensetracker.Userservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import expensetracker.Userservice.entities.UserInfoDto;
import expensetracker.Userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceConsumer {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthServiceConsumer(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic-json.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(@Payload UserInfoDto eventData) {
        log.info("📩 Kafka message received - User: {}, Email: {}",
                eventData.getUserId(), eventData.getEmail());

        try {
            userService.createOrUpdateUser(eventData);
            log.info("✅ User saved successfully: {}", eventData.getUserId());
        } catch (Exception ex) {
            log.error("❌ Error processing Kafka event for user: {}", eventData.getUserId(), ex);
        }
    }
}

//package expensetracker.Userservice.consumer;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import expensetracker.Userservice.entities.UserInfoDto;
//import expensetracker.Userservice.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthServiceConsumer
//{
//
//    private final  UserService userService;
//
//    private final ObjectMapper objectMapper;
//
//    @Autowired
//    public AuthServiceConsumer(UserService userService, ObjectMapper objectMapper) {
//        this.userService = userService;
//        this.objectMapper = objectMapper;
//    }
//
////    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")
////    public void listen(UserInfoDto eventData) {
////        System.out.println("🔥 KAFKA HIT! Received Raw Data: " + eventData);
////        try{
////            // Todo: Make it transactional, to handle idempotency and validate email, phoneNumber etc
////            userService.createOrUpdateUser(eventData);
////        }catch(Exception ex){
////            ex.printStackTrace();
////            System.out.println("AuthServiceConsumer: Exception is thrown while consuming kafka event");
////        }
////    }
//@KafkaListener(
//        topics = "${spring.kafka.topic-json.name}",
//        groupId = "${spring.kafka.consumer.group-id}",
//        containerFactory = "kafkaListenerContainerFactory"
//)
//public void listen(
//        @Payload UserInfoDto eventData,
//        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
//        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
//        @Header(KafkaHeaders.OFFSET) long offset
//) {
//    System.out.println("\n==========================================");
//    System.out.println("🔥 KAFKA MESSAGE RECEIVED!");
//    System.out.println("📍 Topic: " + topic);
//    System.out.println("📦 Partition: " + partition);
//    System.out.println("🔢 Offset: " + offset);
//    System.out.println("📄 Data: " + eventData);
//
//    if (eventData != null) {
//        System.out.println("👤 User ID: " + eventData.getUserId());
//        System.out.println("📧 Email: " + eventData.getEmail());
//        System.out.println("👤 Name: " + eventData.getFirstName() + " " + eventData.getLastName());
//    } else {
//        System.err.println("⚠️ Received NULL eventData!");
//    }
//    System.out.println("==========================================\n");
//
//    try {
//        userService.createOrUpdateUser(eventData);
//        System.out.println("✅ User saved successfully to database!");
//    } catch (Exception ex) {
//        System.err.println("❌ ERROR while processing Kafka event:");
//        ex.printStackTrace();
//    }
//}
//
//}