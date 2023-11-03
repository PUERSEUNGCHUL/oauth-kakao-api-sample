package kr.co.puerpuella.oathserver.api.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.co.puerpuella.oathserver.api.dto.MemberInfoDto;
import kr.co.puerpuella.oathserver.api.dto.SocialLoginCode;
import kr.co.puerpuella.oathserver.api.dto.form.LoginForm;
import kr.co.puerpuella.oathserver.api.dto.result.LoginDto;
import kr.co.puerpuella.oathserver.api.util.SocialUtil;
import kr.co.puerpuella.oathserver.common.enums.ErrorInfo;
import kr.co.puerpuella.oathserver.common.framework.CommonDTO;
import kr.co.puerpuella.oathserver.common.framework.CommonService;
import kr.co.puerpuella.oathserver.common.framework.exception.ValidationException;
import kr.co.puerpuella.oathserver.common.framework.response.CommonReturnData;
import kr.co.puerpuella.oathserver.common.framework.response.ResponseBody;
import kr.co.puerpuella.oathserver.model.entity.Member;
import kr.co.puerpuella.oathserver.model.repositories.MemberRepository;
import kr.co.puerpuella.oathserver.security.SecurityUtil;
import kr.co.puerpuella.oathserver.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SocialLoginService extends CommonService{

    private final LoginService loginService;

    private final SocialUtil socialUtil;

    private final MemberRepository memberRepository;

    private final TokenProvider tokenProvider;


    private final JsonParser parser = new JsonParser();

    public CommonReturnData execute(CommonDTO... params) {

        SocialLoginCode codeDTO = (SocialLoginCode) params[0];


        try {

            // 토큰 요청관련 정보 설정
            URL tokenProviderURL = new URL(socialUtil.getTokenProviderURL(codeDTO.getProvider()));
            HttpURLConnection tokenProviderConnection = (HttpURLConnection) tokenProviderURL.openConnection();

            // 토큰 요청
            sendRequest(tokenProviderConnection, codeDTO);

            // 토큰 응답
            String responseStr = receiveResponse(tokenProviderConnection);

            System.out.println("responseStr = " + responseStr);

            LoginForm memberInfo = null;
            if ("google".equals(codeDTO.getProvider())) {
                memberInfo = findMemberForJWT(responseStr, codeDTO);
            } else if ("kakao".equals(codeDTO.getProvider()) || "naver".equals(codeDTO.getProvider())) {

                memberInfo = findMemberForSearchUserInfo(responseStr, codeDTO);
            }


            Member savedMember = memberRepository.findOneByEmail(memberInfo.getEmail());

            // 가입되어 있는 회원인지 체크
            if (savedMember == null) {
                throw new ValidationException(ErrorInfo.LOGIN_NO_EMAIL);
            }



            // 신규 액세스토큰 발급
            String accessToken = tokenProvider.createToken(savedMember);

            Authentication authentication = tokenProvider.getAuthentication(accessToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 응답Header에 토큰 저장
            tokenProvider.setTokenToHeader(accessToken);

            return LoginDto.builder().accessToken(accessToken).build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidationException(ErrorInfo.SYSTEM_ERROR);
        }
    }

    private LoginForm findMemberForJWT(String responseStr, SocialLoginCode codeDTO) throws Exception {
        JsonObject jo = (JsonObject) parser.parse(responseStr);

        String jwt = jo.get("id_token").getAsString();

        Base64.Decoder decoder = Base64.getUrlDecoder();

        String decodedJWT = String.valueOf(decoder.decode(jwt));

        JsonObject jwtJO = (JsonObject) parser.parse(decodedJWT);

        return LoginForm.builder()
                .email(jwtJO.get("email").getAsString())
                .provider(codeDTO.getProvider())
                .nickname(jwtJO.get("name").getAsString())
                .providerId(jwtJO.get("sub").getAsString())
                .isSocial(true)
                .build();
    }



    private LoginForm findMemberForSearchUserInfo(String responseStr, SocialLoginCode codeDTO) throws Exception {
        // 응답에서 토큰값 꺼내기
        String socialAccessToken = getAccessToken(responseStr);

        // 유저정보 요청관련 정보 설정
        URL userInfoProviderURL = new URL(socialUtil.getUserInfoProviderURL(codeDTO.getProvider()));
        HttpURLConnection userInfoProviderConnection = (HttpURLConnection) userInfoProviderURL.openConnection();

        // 유저정보 요청
        sendRequestForUserInfo(userInfoProviderConnection, socialAccessToken);

        // 유저정보 응답
        String userInfoResponseStr = receiveUserInfo(userInfoProviderConnection);

        return getUserInfo(userInfoResponseStr, codeDTO);
    }

    private LoginForm getUserInfo (String response, SocialLoginCode codeDTO) throws Exception{
        System.out.println("response = " + response);
        JsonObject jo = (JsonObject) parser.parse(response.toString());

        if (jo == null) {
            throw new Exception("유저정보가 존재하지 않습니다.");
        }

        String socialId = null;
        String nickname = null;
        String email = null;


        switch (codeDTO.getProvider()) {
            case "kakao" : {
                socialId = jo.get("id").getAsLong()+"";
                nickname = jo.get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("nickname").getAsString();
                email = jo.get("kakao_account").getAsJsonObject().get("email").getAsString();

                break;
            }
            case "naver" : {
                socialId = jo.getAsJsonObject("response").get("id").getAsString();
                nickname = jo.getAsJsonObject("response").get("nickname").getAsString();
                email = jo.getAsJsonObject("response").get("email").getAsString();

                break;
            }
            case "google" : {
                socialId = jo.get("id").getAsString();
                nickname = jo.get("name").getAsString();
                email = jo.get("email").getAsString();

                break;
            }

            default: {
                throw new ValidationException(ErrorInfo.SYSTEM_ERROR);
            }
        }
        return LoginForm.builder()
                .email(email)
                .provider(codeDTO.getProvider())
                .nickname(nickname)
                .providerId(socialId)
                .isSocial(true)
        .build();
    }

    private String receiveUserInfo(HttpURLConnection conn) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        StringBuilder responseBuilder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            responseBuilder.append(line);
        }

        return responseBuilder.toString();
    }

    private void sendRequestForUserInfo(HttpURLConnection conn, String accessToken) throws Exception{
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
    }

    private String getAccessToken(String response) throws Exception{
        JsonObject jo = (JsonObject) parser.parse(response);

        if (jo == null) {
            throw new Exception("Access토큰을 받지 못했습니다.");
        }

        System.out.println("response = " + response);

        return jo.get("access_token").getAsString();
    }

    private String receiveResponse(HttpURLConnection conn) throws Exception {
        int responseCode = conn.getResponseCode();
        System.out.println(responseCode);


        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        StringBuilder responseBuilder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            responseBuilder.append(line);
        }

        return responseBuilder.toString();
    }
    private void sendRequest(HttpURLConnection conn, SocialLoginCode codeDTO) throws Exception{
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        //TODO 카카오기준으로 작성(구글/페이스북/네이버 등이 다를경우 인터페이스화 필요)
        sb.append("grant_type=authorization_code");
        sb.append("&client_id="+socialUtil.getClientId(codeDTO.getProvider()));
        sb.append("&redirect_uri="+socialUtil.getRedirectUri(codeDTO.getProvider()));
        sb.append("&code="+codeDTO.getCode());
        sb.append("&client_secret="+socialUtil.getSecretKey(codeDTO.getProvider()));
        bw.write(sb.toString());

        bw.flush();
    }

}
