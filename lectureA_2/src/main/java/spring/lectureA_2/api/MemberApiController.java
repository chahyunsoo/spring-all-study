package spring.lectureA_2.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.bind.annotation.*;
import spring.lectureA_2.domain.Member;
import spring.lectureA_2.service.MemberService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@Controller @ResponseBody
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * Entity에 프레젠테이션 계층을 위한 로직이 추가된다, 화면을 뿌리기 위한 로직이 추가됐다. => Entity에 @JsonIgnore을 추가했음.
     * Entity로 의존관계가 들어와야 하는데 v1은 반대로 Entity에서 의존관계가 나가기 때문에 양방향 의존관계로 인해 유지보수 어려움.
     *
     * 같은 Entity에 대해서 다양한 API가 존재하는데, 하나의 Entity에 여러개의 Entity 각각을 위한 로직을 추가하기는 어려움.
     * v1은 Entity가 변경되면 API 스펙이 변경된다.
     *
     * 그래서 API 응답 스펙에 맞춰 DTO를 반환해야 한다.
     * @return
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        List<Member> members = memberService.findMembers();
        return members;
    }

    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();

        List<MemberDto> collect = new ArrayList<>();
        for (Member findMember : findMembers) {
            MemberDto memberDto = new MemberDto(findMember.getName());
            collect.add(memberDto);
        }

//        List<MemberDto> collect = findMembers.stream()
//                .map(m -> new MemberDto(m.getName()))
//                .collect(Collectors.toList());

        Result result = new Result(collect);
        return result;
    }

    //첫번째 버전의 회원 등록 api
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) { //json 데이터를 인자로 들어온 Member객체에 매핑시켜서 바꿈
        Long id = memberService.join(member);
        CreateMemberResponse createMemberResponse = new CreateMemberResponse(id);
        return createMemberResponse;
    }

    //두번째 버전의 회원 등록 api
    //요청 값으로 Member Entity 대신에 별도의 DTO를 둠
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * v1과 v2를 비교했을때 v1이 주는 유일한 장점은 DTO 를 안 만들어도 된다는 것이다. 그런데 그것이 과연 장점일까...?
     * v1은 인자로 Member 객체가 들어왔기 때문에 API 스펙 문서를 까보지 않는 이상 Member 객체의 필드들 중에  어느 값이 인자로 넘어오는지 알기 힘들다.
     * 현재 Member 객체를 예시로 봤을 때는 id,name,address,orders 필드들이 있는데 id만 넘어오는지, name도 같이 넘어오는지 ,address는 안넘오는지 등등.. Entity 만으로는 알기 힘들다.
     * <p>
     * 하지만 v2 처럼 DTO를 인자로 넘기게 되면 DTO 클래스를 보고 API 스펙자체가 DTO 클래스에 존재하는 필드들만 받게 되있구나 라고 알 수 있다.
     * <p>
     * v2는 만약에 누군가 Entity의 특정 필드명을 변경했다고 했을때(ex. name -> username 변경시)
     * member.setName(...)을 member.setUsername(...)으로만 변경하면 된다. Entity를 변경해도 API스펙이 바뀌지 않는다.
     * <p>
     * API를 만들때는 항상 Entity를 인자로 받지 않는다. Entity를 외부에 노출시켜서도 안된다.
     * v2는 별도의 요청 응답 DTO를 만든 것.
     *
     * @NotEmpty 어노테이션도 Entity에 사용하는 것보다 DTO에서 사용하는 것이 유지보수적인 측면에서도 좋다.
     */

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member member = memberService.findOne(id); // -> 쿼리를 별도로 짬.

        UpdateMemberResponse updateMemberResponse = new UpdateMemberResponse(member.getId(), member.getName());
        return updateMemberResponse;
    }



    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }


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

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
}
