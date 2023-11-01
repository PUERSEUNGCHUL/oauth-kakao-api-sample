package kr.co.puerpuella.oathserver.api.service;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.puerpuella.oathserver.api.dto.KakaoTokenDTO;
import kr.co.puerpuella.oathserver.common.framework.CommonDTO;
import kr.co.puerpuella.oathserver.common.framework.CommonService;
import kr.co.puerpuella.oathserver.common.framework.response.CommonReturnData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class KakaoLoginService extends CommonService {
    private final LoginService loginService;

    private final String oauthCodeHeaderKey;
    private final String oauthVerifyURL;
    private final String oauthRestApiKey;
    private final String oauthRedirectURI;

    private final JsonParser parser = new JsonParser();

    private final RestTemplate restTemplate;

    public KakaoLoginService(LoginService loginService, @Value("${oauth.verify-header-key}") String oauthCodeHeaderKey,
                             @Value("${oauth.verify-url}") String oauthVerifyURL,
                             @Value("${oauth.rest-api-key}") String oauthRestApiKey,
                             @Value("${oauth.verify-redirect-uri}") String oauthRedirectURI,
                             RestTemplateBuilder restTemplateBuilder) {
        this.loginService = loginService;
        this.oauthCodeHeaderKey = oauthCodeHeaderKey;
        this.oauthVerifyURL = oauthVerifyURL;
        this.oauthRestApiKey = oauthRestApiKey;
        this.oauthRedirectURI = oauthRedirectURI;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    protected CommonReturnData execute(CommonDTO... params)  {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String oauthCode = request.getHeader(this.oauthCodeHeaderKey);

        try {

            URL url = new URL("https://kauth.kakao.com/oauth/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+this.oauthRestApiKey);
            sb.append("&redirect_uri="+this.oauthRedirectURI);
            sb.append("&code="+oauthCode);
            bw.write(sb.toString());

            bw.flush();



            int responseCode = conn.getResponseCode();
            System.out.println(responseCode);


            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            StringBuilder responseBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line);
            }



            JsonObject jo = (JsonObject) parser.parse(responseBuilder.toString());

            if (jo == null) {
                throw new Exception("Access토큰을 받지 못했습니다.");
            }

            String accessToken = jo.get("access_token").getAsString();

            KakaoTokenDTO kakaoTokenDTO = getKakaoInfo(accessToken);


            return getKakaoInfo(accessToken);
        } catch (Exception e) {

            e.printStackTrace();

            return KakaoTokenDTO.builder().build();
        }

    }

    private KakaoTokenDTO getKakaoInfo(String accessToken) throws Exception {
        URL url = new URL("https://kapi.kakao.com/v2/user/me");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        StringBuilder responseBuilder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            responseBuilder.append(line);
        }

        System.out.println("responseBuilder = " + responseBuilder);

        JsonObject jo = (JsonObject) parser.parse(responseBuilder.toString());

        if (jo == null) {
            throw new Exception("유저정보가 존재하지 않습니다.");
        }


        Long kakaoId = jo.get("id").getAsLong();
        String nickname = jo.get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("nickname").getAsString();

        return KakaoTokenDTO.builder().kakaoId(kakaoId).nickname(nickname).build();

    }
}
