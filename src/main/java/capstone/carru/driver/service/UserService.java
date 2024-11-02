package capstone.carru.driver.service;

import static capstone.carru.driver.dto.ErrorCode.INVALID_DUPLICATED_EMAIL;

import capstone.carru.driver.dto.CreateUserRequest;
import capstone.carru.driver.entity.User;
import capstone.carru.driver.repository.UserRepository;
import capstone.carru.exception.InvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void createUser(CreateUserRequest createUserRequest) {
        //1. 이미 가입한 회원인지 확인
        if (userRepository.findByEmailAndDeletedDateIsNull(createUserRequest.getEmail()).isPresent()) {
            throw new InvalidException(INVALID_DUPLICATED_EMAIL);
        }

        //2. 가입한 회원이 아니라면 회원가입 진행
        User user = User.builder()
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword())
                .name(createUserRequest.getName())
                .phoneNumber(createUserRequest.getPhoneNumber())
                .carLocation(createUserRequest.getCarLocation())
                .build();

        userRepository.save(user);
    }
}
