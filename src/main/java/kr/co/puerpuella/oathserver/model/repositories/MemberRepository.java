package kr.co.puerpuella.oathserver.model.repositories;

import kr.co.puerpuella.oathserver.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    /** UID로 회원 조회*/
    Member findOneByUid(Long uid);

    Member findOneByEmail(String email);

    Member findOneByNickname(String nickname);
}
