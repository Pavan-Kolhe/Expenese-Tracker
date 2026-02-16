//package authservice.serializer;
//
//import authservice.model.UserInfoDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.kafka.common.header.Headers;
//import org.apache.kafka.common.serialization.Serializer;
//
//import java.util.Map;
//
//public class UserInfoSerializer implements Serializer{
//    @Override
//    public void configure(Map configs, boolean isKey) {
//    }
//
//    @Override
//    public byte[] serialize(String arg0, UserInfoDto arg1) {
//        byte[]  retVal = null;
//        ObjectMapper objectMapper = new ObjectMapper();
//        try{
//            retVal = objectMapper.writeValueAsString(arg1).getBytes();
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return retVal;
//    }
//
//
//    @Override
//    public byte[] serialize(String s, Object o) {
//    }
//
//    @Override
//    public byte[] serialize(String topic, Headers headers, Object data) {
//    }
//
//    @Override
//    public void close() {
//    }
//}
package authservice.serializer;

import authservice.eventProducer.UserInfoEvent;
import authservice.model.UserInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserInfoSerializer implements Serializer<UserInfoEvent>
{
    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String arg0, UserInfoEvent arg1) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(arg1).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    @Override public void close() {
    }
}