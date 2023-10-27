package kr.co.puerpuella.prtjwtapiserver.model.entity;

import jakarta.persistence.*;
import kr.co.puerpuella.prtjwtapiserver.common.enums.Roles;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 회원 테이블 엔티티
 */
@Entity
@Table(name="T_MEMBER")
@Getter
@Builder
@RequiredArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long uid;

    @Column(length = 10, nullable = false)
    private String nickname;

    private String password;


    private Roles role;
}
