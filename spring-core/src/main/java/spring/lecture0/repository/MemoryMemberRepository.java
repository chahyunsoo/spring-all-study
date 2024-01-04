package spring.lecture0.repository;

import org.springframework.stereotype.Repository;
import spring.lecture0.domain.Member;

import java.util.*;


public class MemoryMemberRepository implements MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();  //공유되는 변수일때, 동시성 문제 발생 가능하긴 함
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()  //Map에 저장된 모든 값을 컬렉션 형태로 반환,그 결과로 Stream 객체가 반환
                .filter(member -> member.getName().equals(name))  //내용 비교
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        Collection<Member> values = store.values();
        //Collection<Member>가 실제로는 List<Member> 형태가 아닐 수 있기 때문
        //List<Member> member = (List<Member>) store.values(); 이건 지양
        return new ArrayList<>(values);
    }

    public void clearStore() {
        store.clear();
    }
}
