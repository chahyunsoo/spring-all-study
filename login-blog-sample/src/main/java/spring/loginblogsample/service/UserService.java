package spring.loginblogsample.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.loginblogsample.config.BCryptConfig;
import spring.loginblogsample.domain.User;
import spring.loginblogsample.dto.JoinRequest;
import spring.loginblogsample.dto.LoginRequest;
import spring.loginblogsample.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
//    private final BCryptConfig bCryptConfig;

    //loginId 중복체크
    public boolean checkDuplicateLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    //nickname 중복체크
    public boolean checkDuplicateNickName(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //회원가입 -> 비밀번호 암호화 X
    public void joinWithOutEncodedPassword(JoinRequest joinRequest) {
        userRepository.save(joinRequest.toEntityWithOutEncodedPassword());
    }

    //회원가입 -> 비밀번호 암호화 O
//    public void joinWithEncodedPassword(JoinRequest joinRequest) {
//        userRepository.save(joinRequest.toEntityWithEncodedPassword(bCryptConfig.passwordEncoder()
//                .encode(joinRequest.getPassword())
//        ));
//    }

    //로그인 -> loginId가 DB에 없으면 로그인 X / 비밀번호가 일치하지 않으면 로그인 X
    public User login(LoginRequest loginRequest) {
        Optional<User> userByLoginId = userRepository.findByLoginId(loginRequest.getLoginId());
        if (userByLoginId.get().getUserId() == null) {
            return null;
        }
        if (!userByLoginId.get().getPassword().equals(loginRequest.getPassword())) {
            return null;
        }
        return userByLoginId.get();
    }

    //userId를 이용하여 User return -> 추후 인증, 인가 시 사용예정
    public User returnUserByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        if (userRepository.findById(userId) == null) {
            return null;
        }
        return userRepository.findById(userId).get();
    }

    //loginId를 이용하여 User return -> 추후 인증, 인가 시 사용예정
    public User returnUserByLoginId(String loginId) {
        if (loginId == null) {
            return null;
        }
        if (userRepository.findByLoginId(loginId) == null) {
            return null;
        }
        return userRepository.findByLoginId(loginId).get();
    }

}

