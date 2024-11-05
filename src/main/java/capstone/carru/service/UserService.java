package capstone.carru.service;

import capstone.carru.dto.ErrorCode;
import capstone.carru.dto.User.CreateUserRequest;
import capstone.carru.dto.User.LoginRequest;
import capstone.carru.entity.User;
import capstone.carru.exception.NotFoundException;
import capstone.carru.repository.UserRepository;
import capstone.carru.exception.InvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void createUser(CreateUserRequest createUserRequest) {
        //1. 이미 가입한 회원인지 확인
        if (userRepository.findByEmailAndDeletedDateIsNull(createUserRequest.getEmail()).isPresent()) {
            throw new InvalidException(ErrorCode.INVALID_DUPLICATED_EMAIL);
        }

        //2. 가입한 회원이 아니라면 회원가입 진행
        User user = User.builder()
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword())
                .name(createUserRequest.getName())
                .phoneNumber(createUserRequest.getPhoneNumber())
                .location(createUserRequest.getCarLocation())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public String loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmailAndDeletedDateIsNull(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_MEMBER));

        if(user.getApprovedDate() == null) {
            throw new InvalidException(ErrorCode.INVALID_MEMBER);
        }

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new InvalidException(ErrorCode.INVALID_PASSWORD);
        }

        return jwtTokenProvider.generateToken(user.getEmail(), "USER");
    }
}
