package kr.co.puerpuella.oathserver.api.service;

import kr.co.puerpuella.oathserver.api.dto.MemberInfoDto;
import kr.co.puerpuella.oathserver.api.dto.form.ViewForm;
import kr.co.puerpuella.oathserver.common.enums.ErrorInfo;
import kr.co.puerpuella.oathserver.common.framework.CommonDTO;
import kr.co.puerpuella.oathserver.common.framework.CommonService;
import kr.co.puerpuella.oathserver.common.framework.exception.ValidationException;
import kr.co.puerpuella.oathserver.common.framework.response.CommonReturnData;
import kr.co.puerpuella.oathserver.model.entity.Member;
import kr.co.puerpuella.oathserver.model.repositories.MemberRepository;
import kr.co.puerpuella.oathserver.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberViewService extends CommonService {

    private final MemberRepository memberRepository;
    @Override
    protected CommonReturnData execute(CommonDTO... params) {

        ViewForm form = (ViewForm) params[0];

        Long uid = SecurityUtil.getCurrentUserId();

        System.out.println("uid = " + uid);

        if (!form.getUid().equals(uid)) {
            throw new ValidationException(ErrorInfo.SYSTEM_ERROR);
        }

        Member member = memberRepository.findOneByUid(form.getUid());

        return MemberInfoDto.builder()
                .uid(member.getUid())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .role(member.getRole())
                .provider(member.getProvider())
                .providerId(member.getProviderId())
                .build();
    }
}
