package kr.co.puerpuella.oathserver.api.service;


import kr.co.puerpuella.oathserver.api.dto.MemberInfoDto;
import kr.co.puerpuella.oathserver.api.dto.form.JoinForm;
import kr.co.puerpuella.oathserver.common.enums.ErrorInfo;
import kr.co.puerpuella.oathserver.common.framework.CommonDTO;
import kr.co.puerpuella.oathserver.common.framework.CommonService;
import kr.co.puerpuella.oathserver.common.framework.exception.ValidationException;
import kr.co.puerpuella.oathserver.common.framework.response.CommonReturnData;
import kr.co.puerpuella.oathserver.model.entity.Member;
import kr.co.puerpuella.oathserver.model.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원가입 서비스
 */
@Service
@RequiredArgsConstructor
public class MemberJoinService extends CommonService {
    
    /** 패스워드 암호화 모듈 */
    private final PasswordEncoder passwordEncoder;
    
    /** 회원 레파지토리 */
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public CommonReturnData execute(CommonDTO... params) {


        JoinForm joinForm  = (JoinForm)params[0];

        //닉네임 중복 체크
        if (checkDuplicateNickname(joinForm)) {
            throw new ValidationException(ErrorInfo.JOIN_DUPLICATED_NICKNAME);
        }

        //이메일 중복 체크
        if (checkDuplicateEmail(joinForm)) {
            throw new ValidationException(ErrorInfo.JOIN_DUPLICATED_EMAIL);
        }

        // 회원가입하려는 멤버의 패스워드 암호화
        joinForm.encryptPassword(passwordEncoder);

        Member joinMember = joinForm.toEntity();

        Member newMember = memberRepository.save(joinMember);

        return MemberInfoDto.builder()
                .uid(newMember.getUid())
                .email(newMember.getEmail())
                .nickname(newMember.getNickname())
                .role(newMember.getRole())
                .provider(newMember.getProvider())
        .build();

    }

    private boolean checkDuplicateEmail(JoinForm joinForm) {
        Member joinedMember = memberRepository.findOneByEmail(joinForm.getEmail());

        return joinedMember != null;
    }

    private boolean checkDuplicateNickname(JoinForm joinForm) {
        Member joinedMember = memberRepository.findOneByNickname(joinForm.getNickname());

        return joinedMember != null;
    }
}
