package kr.co.puerpuella.oathserver.api.dto.result;

import kr.co.puerpuella.oathserver.common.framework.response.CommonReturnData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginDto extends CommonReturnData {

    private String accessToken;
}
