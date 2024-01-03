package spring.lectureA_2.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {
    private final EntityManager em;

    //Repository는 가급적 순수한 Entity를 조회하는데 쓰는데
    //아래 코드는 조회 전용으로 화면에 맞춰서 쓰는 느낌이니까 따로 분리
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "select new spring.lectureA_2.repository.order.simplequery.OrderSimpleQueryDto(o.id,m.name,o.orderDate,o.status,d.address  )" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDto.class
        ).getResultList();
    }
}
