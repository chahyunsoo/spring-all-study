package spring.loginblogsample.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.loginblogsample.config.BCryptConfig;
import spring.loginblogsample.domain.Member;
import spring.loginblogsample.dto.JoinRequest;
import spring.loginblogsample.dto.LoginRequest;
import spring.loginblogsample.repository.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptConfig bCryptConfig;

    //loginId 중복체크
    public boolean checkDuplicateLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    //nickname 중복체크
    public boolean checkDuplicateNickName(String nickname) {
        return memberRepository.existsByNickName(nickname);
    }

    //회원가입 -> 비밀번호 암호화 X
    public void joinWithOutEncodedPassword(JoinRequest joinRequest) {
        memberRepository.save(joinRequest.toEntityWithOutEncodedPassword());
    }

    //회원가입 -> 비밀번호 암호화 O
    public void joinWithEncodedPassword(JoinRequest joinRequest) {
        memberRepository.save(joinRequest.toEntityWithEncodedPassword(bCryptConfig.passwordEncoder()
                .encode(joinRequest.getPassword())
        ));
    }

    //로그인 -> loginId가 DB에 없으면 로그인 X / 비밀번호가 일치하지 않으면 로그인 X
    public Member login(LoginRequest loginRequest) {
        Optional<Member> memberByLoginId = memberRepository.findByLoginId(loginRequest.getLoginId());
        if (memberByLoginId.get().getMemberId() == null) {
            return null;
        }
        if (!memberByLoginId.get().getPassword().equals(loginRequest.getPassword())) {
            return null;
        }
        return memberByLoginId.get();
    }

    //memberId를 이용하여 Member return -> 추후 인증, 인가 시 사용예정
    public Member returnByMemberId(Long memberId) {
        if (memberId == null) {
            return null;
        }
        if (memberRepository.findById(memberId) == null) {
            return null;
        }
        return memberRepository.findById(memberId).get();
    }

    //loginId를 이용하여 Member return -> 추후 인증, 인가 시 사용예정
    public Member returnByLoginId(String loginId) {
        if (loginId == null) {
            return null;
        }
        if (memberRepository.findByLoginId(loginId) == null) {
            return null;
        }
        return memberRepository.findByLoginId(loginId).get();
    }

}

