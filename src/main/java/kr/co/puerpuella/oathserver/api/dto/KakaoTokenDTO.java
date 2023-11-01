package kr.co.puerpuella.oathserver.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import kr.co.puerpuella.oathserver.common.framework.response.CommonReturnData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
public class KakaoTokenDTO extends CommonReturnData {
    private Long kakaoId;
    private String nickname;


}
