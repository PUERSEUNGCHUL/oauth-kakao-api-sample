package kr.co.puerpuella.oathserver.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KaKaoMemberInfo {

    private Long id;

    private String connectedAt;

    private KakaoAccount kakaoAccount;

}
