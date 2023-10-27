package kr.co.puerpuella.prtjwtapiserver.api.service;

import kr.co.puerpuella.prtjwtapiserver.security.JwtFilter;
import kr.co.puerpuella.prtjwtapiserver.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginService extends CommonService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 패스워드 암호화 모듈
     */
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected CommonReturnData execute(CommonDTO... params) {

        LoginForm loginForm = (LoginForm) params[0];

        Member savedMember = memberRepository.findOneByEmail(loginForm.getEmail());

        //가입되어 있는 회원인지 체크
        if (savedMember == null) {

            throw new ValidationException(ErrorInfo.LOGIN_NO_EMAIL);
        }

        //패스워드 확인
        if (!passwordEncoder.matches(loginForm.getPassword(), savedMember.getPassword())) {

            throw new ValidationException(ErrorInfo.LOGIN_INVALID_PASSWORD);
        }

        // 신규 액세스토큰 발급
        String accessToken = createAccessToken(loginForm, savedMember);
        // 신규 리프레시토큰 발급
        String refreshTokenJwt = tokenProvider.createRefreshToken();

        // 응답Header에 두 토큰 저장
        setTokenToHeader(accessToken, refreshTokenJwt);

        // Redis에 리프레시토큰 저장
        saveRefreshTokenToRedis(savedMember, refreshTokenJwt);

        return new LoginDto(accessToken, refreshTokenJwt);

    }

    /**
     * Redis에 Refresh토큰을 저장한다.
     *
     * @param savedMember
     * @param refreshTokenJwt
     */
    private void saveRefreshTokenToRedis(Member savedMember, String refreshTokenJwt) {
        RefreshToken refreshToken = new RefreshToken(refreshTokenJwt, savedMember.getId());

        refreshTokenRepository.save(refreshToken);
    }

    /**
     * 발급받은 Access,Refresh 토큰을 응답Header에 설정한다.
     *
     * @param accessToken
     * @param refreshTokenJwt
     */
    private static void setTokenToHeader(String accessToken, String refreshTokenJwt) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);
        httpHeaders.add(JwtFilter.REFRESH_HEADER, refreshTokenJwt);
    }

    /**
     * 새로운 Access토큰을 생성한다.
     *
     * @param loginForm
     * @param savedMember
     * @return
     */
    private String createAccessToken(LoginForm loginForm, Member savedMember) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(savedMember.getId(), loginForm.getPassword());

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String authorities = authenticate.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining("."));

        String jwt = tokenProvider.createAccessToken(authenticate.getName(), authorities);
        return jwt;
    }

    /**
     * UID로 회원정보를 조회한다.
     *
     * @param uid the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {

        Member member = memberRepository.findOneById(Long.parseLong(uid));

        return new User(member.getId().toString(), member.getPassword(), member.getAuthorities());
    }
}