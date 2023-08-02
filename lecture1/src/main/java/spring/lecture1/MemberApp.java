package spring.lecture1;

import spring.lecture1.member.Grade;
import spring.lecture1.member.MemberServiceImpl;
import spring.lecture1.member.Member;
import spring.lecture1.member.MemberService;

public class MemberApp {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        Member member1 = new Member(1L, "choi", Grade.VIP);

        //회원가입 진행
        memberService.join(member1);
        Member findMember = memberService.findMember(1L);

        System.out.println("member1 = " + member1.getName());
        System.out.println("findMember = " + findMember.getName());

    }
}
