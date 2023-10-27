package kr.co.puerpuella.prtjwtapiserver.api.dto;

import kr.co.puerpuella.prtjwtapiserver.common.enums.Roles;

import kr.co.puerpuella.prtjwtapiserver.common.framework.response.CommonReturnData;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoDto extends CommonReturnData {

    //UID
    private Long uid;

    //메일주소
    private String email;

    private String nickname;

    private Roles role;
}
