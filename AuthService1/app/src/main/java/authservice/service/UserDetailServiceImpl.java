package authservice.service;

import authservice.entities.UserInfo;
import authservice.eventProducer.UserInfoEvent;
import authservice.eventProducer.UserInfoProducer;
import authservice.model.UserInfoDto;
import authservice.repository.UserRepository;
import authservice.util.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;


@Component
@AllArgsConstructor
@Data
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private  final UserInfoProducer userInfoProducer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        UserInfo user = userRepository.findByUsername(username);
        if(user ==  null){
            throw  new UsernameNotFoundException("could not found the user !!");
        }
        return new CustomUserDetails(user);
    }


    public UserInfo checkIfUserAlreadyExists(UserInfoDto user){
        return userRepository.findByUsername(user.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto){
        if(!ValidationUtil.validateUser(userInfoDto)){
            return false;
        }
            userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));

            if(Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))){
                return  false;
            }

            String userId = UUID.randomUUID().toString();

            userRepository.save(
                    new UserInfo(
                            userId,
                            userInfoDto.getUsername(),
                            userInfoDto.getPassword(),
                            new HashSet<>()
                    )
            );

        // pushEvent to Queue
        userInfoProducer.sendEventToKafka(userInfoEvent(userInfoDto,userId));
        return true;
    }

    private UserInfoEvent userInfoEvent(UserInfoDto userInfoDto,String userId){
        return UserInfoEvent.builder()
                .userId(userId)
                .firstName(userInfoDto.getFirstName())
                .lastName(userInfoDto.getLastName())
                .email(userInfoDto.getEmail())
                .phoneNumber(userInfoDto.getPhoneNumber())
                .build();
    }

}
