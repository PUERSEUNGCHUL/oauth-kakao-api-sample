package kr.co.puerpuella.prtjwtapiserver.api.service;

import kr.co.puerpuella.prtjwtapiserver.api.dto.form.LoginForm;
import kr.co.puerpuella.prtjwtapiserver.api.dto.result.LoginDto;
import kr.co.puerpuella.prtjwtapiserver.common.enums.ErrorInfo;
import kr.co.puerpuella.prtjwtapiserver.common.framework.CommonDTO;
import kr.co.puerpuella.prtjwtapiserver.common.framework.CommonService;
import kr.co.puerpuella.prtjwtapiserver.common.framework.exception.ValidationException;
import kr.co.puerpuella.prtjwtapiserver.common.framework.response.CommonReturnData;
import kr.co.puerpuella.prtjwtapiserver.model.entity.Member;
import kr.co.puerpuella.prtjwtapiserver.model.repositories.MemberRepository;
import kr.co.puerpuella.prtjwtapiserver.security.JwtFilter;
import kr.co.puerpuella.prtjwtapiserver.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService extends CommonService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /** 패스워드 암호화 모듈 */
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;


    @Override
    protected CommonReturnData execute(CommonDTO... params) {

        LoginForm loginForm = (LoginForm) params[0];

        Member savedMember = memberRepository.findOneByEmail(loginForm.getEmail());

        //가입되어 있는 회원인지 체크
        if (savedMember == null) {

            throw new ValidationException(ErrorInfo.LOGIN_NO_EMAIL);
        }

        //패스워드 확인
        if (!passwordEncoder.matches(loginForm.getPassword(), savedMember.getPassword())){

            throw new ValidationException(ErrorInfo.LOGIN_INVALID_PASSWORD);
        }

        // 신규 액세스토큰 발급
        String accessToken = createAccessToken(loginForm, savedMember);

        // 응답Header에 토큰 저장
        setTokenToHeader(accessToken);

        return new LoginDto(accessToken);
    }

    /**
     * 새로운 Access토큰을 생성한다.
     * @param loginForm
     * @param savedMember
     * @return
     */
    private String createAccessToken(LoginForm loginForm, Member savedMember) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(savedMember.getUid(), loginForm.getPassword());

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String jwt = tokenProvider.createToken(authenticate);
        return jwt;
    }

    /**
     * 발급받은 Access,Refresh 토큰을 응답Header에 설정한다.
     * @param accessToken
     */
    private static void setTokenToHeader(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);
    }

    /**
     * UID로 회원정보를 조회한다.
     * @param uid the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {

        Member member = memberRepository.findOneByUid(Long.parseLong(uid));

        return new User(member.getUid().toString(),member.getPassword(), member.getAuthorities());
    }
}
