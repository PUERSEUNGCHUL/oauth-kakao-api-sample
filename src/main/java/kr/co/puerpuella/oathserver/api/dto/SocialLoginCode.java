package kr.co.puerpuella.oathserver.api.dto;

import kr.co.puerpuella.oathserver.common.framework.CommonDTO;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SocialLoginCode extends CommonDTO {

    private String provider;

    private String code;
}
