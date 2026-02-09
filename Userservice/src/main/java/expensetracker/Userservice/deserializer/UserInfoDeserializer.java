package expensetracker.Userservice.deserializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import expensetracker.Userservice.entities.UserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class UserInfoDeserializer implements Deserializer<UserInfoDto> {

    private ObjectMapper objectMapper;

    public UserInfoDeserializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.findAndRegisterModules();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // ObjectMapper already configured in constructor
    }

    @Override
    public UserInfoDto deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            log.warn("Received null or empty data from topic: {}", topic);
            return null;
        }

        try {
            return objectMapper.readValue(data, UserInfoDto.class);
        } catch (Exception e) {
            String jsonString = new String(data, StandardCharsets.UTF_8);
            log.error("Failed to deserialize message from topic {}: {}", topic, jsonString, e);
            throw new RuntimeException("Deserialization failed for topic: " + topic, e);
        }
    }

    @Override
    public void close() {
        // No resources to clean up
    }
}


//package expensetracker.Userservice.deserializer;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.PropertyNamingStrategies;
//import expensetracker.Userservice.entities.UserInfoDto;
//import org.apache.kafka.common.serialization.Deserializer;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Map;
//
//public class UserInfoDeserializer implements Deserializer<UserInfoDto> {
//
//    private ObjectMapper objectMapper;
//
//    public UserInfoDeserializer() {
//        this.objectMapper = new ObjectMapper();
//        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        this.objectMapper.findAndRegisterModules();
//        System.out.println("✅✅✅ UserInfoDeserializer CONSTRUCTOR!");
//    }
//
//    @Override
//    public void configure(Map<String, ?> configs, boolean isKey) {
//        System.out.println("🔧 UserInfoDeserializer.configure() called");
//    }
//
//    @Override
//    public UserInfoDto deserialize(String topic, byte[] data) {
//        System.out.println("\n════════════════════════════════════════");
//        System.out.println("🔍 DESERIALIZE CALLED!");
//        System.out.println("════════════════════════════════════════");
//
//        if (data == null || data.length == 0) {
//            System.err.println("⚠️ Data is null or empty!");
//            return null;
//        }
//
//        String jsonString = new String(data, StandardCharsets.UTF_8);
//        System.out.println("📄 JSON: " + jsonString);
//
//        try {
//            UserInfoDto user = objectMapper.readValue(data, UserInfoDto.class);
//            System.out.println("✅ Deserialization SUCCESS!");
//            System.out.println("👤 User: " + user.getUserId());
//            System.out.println("════════════════════════════════════════\n");
//            return user;
//
//        } catch (Exception e) {
//            System.err.println("❌ DESERIALIZATION FAILED!");
//            System.err.println("JSON: " + jsonString);
//            e.printStackTrace();
//            System.out.println("════════════════════════════════════════\n");
//            throw new RuntimeException("Deserialization failed", e);
//        }
//    }
//
//    @Override
//    public void close() {
//    }
//}











// by yt sir

//
//package expensetracker.Userservice.deserializer;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import expensetracker.Userservice.entities.UserInfoDto;
//import org.apache.kafka.common.serialization.Deserializer;
//
//import java.util.Map;
//
//public class UserInfoDeserializer implements Deserializer<UserInfoDto>
//{
//    @Override public void close() {
//    }
//    @Override public void configure(Map<String, ?> arg0, boolean arg1) {
//    }
//
//    @Override
//    public UserInfoDto deserialize(String arg0, byte[] arg1) {
//        ObjectMapper mapper = new ObjectMapper();
//
//        mapper.findAndRegisterModules();
//        UserInfoDto user = null;
//        try {
//            user = mapper.readValue(arg1, UserInfoDto.class);
//        } catch (Exception e) {
//            System.err.println("Deserialization Failed for JSON: " + new String(arg1));
//            e.printStackTrace();
//        }
//        if (user != null) {
//            System.out.println("Successfully deserialized user: " + user.getUserId());
//        }
//
//        return user;
//    }
//}