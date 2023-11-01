package kr.co.puerpuella.oathserver.api.dto.form;


import kr.co.puerpuella.oathserver.common.enums.Roles;
import kr.co.puerpuella.oathserver.common.framework.CommonDTO;
import kr.co.puerpuella.oathserver.model.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 회원가입 Form
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class JoinForm extends CommonDTO {

    private String email;

    private String nickname;

    private String password;

    public void encryptPassword(PasswordEncoder passwordEncoder) {

        this.password = passwordEncoder.encode(this.password);
    }

    public Member toEntity() {
        return Member.builder()
                .email(this.email)
                .nickname(this.nickname)
                .password(this.password)
                .role(Roles.MEMBER)

                .build();
    }
}
