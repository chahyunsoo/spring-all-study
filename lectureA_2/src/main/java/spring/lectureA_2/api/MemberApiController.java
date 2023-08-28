package spring.lectureA_2.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import spring.lectureA_2.domain.Member;
import spring.lectureA_2.service.MemberService;

//@Controller @ResponseBody
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    //첫번째 버전의 회원 등록 api
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        CreateMemberResponse createMemberResponse = new CreateMemberResponse(id);
        return new CreateMemberResponse(id);
    }

    //두번째 버전의 회원 등록 api
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * v1과 v2를 비교했을때 v1이 주는 유일한 장점은 DTO를 안 만들어도 된다는 것이다.
     * v2는 만약에 누군가 Entity의 특정 필드명을 변경했다고 했을때(ex. name -> username 변경시)
     * member.setName(...)을 member.setUsername(...)으로만 변경하면 된다. Entity를 변경해도 API스펙이 바뀌지 않는다.
     *
     */


    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }


}
