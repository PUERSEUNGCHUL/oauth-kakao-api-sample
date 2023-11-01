package kr.co.puerpuella.oathserver.api.dto.form;

import kr.co.puerpuella.oathserver.common.framework.CommonDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인폼
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LoginForm extends CommonDTO {

    private Long kakaoId;
    private String email;
    private String password;
}