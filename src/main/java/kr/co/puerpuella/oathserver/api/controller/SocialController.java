package kr.co.puerpuella.oathserver.api.controller;

import kr.co.puerpuella.oathserver.api.dto.SocialLoginCode;
import kr.co.puerpuella.oathserver.api.service.SocialLoginService;
import kr.co.puerpuella.oathserver.api.util.SocialUtil;
import kr.co.puerpuella.oathserver.common.framework.CommonController;
import kr.co.puerpuella.oathserver.common.framework.response.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SocialController extends CommonController {

    private final SocialUtil socialUtil;

    private final SocialLoginService socialLoginService;

    @GetMapping("/members/login/social/{provider}")
    public ResponseEntity socialLoginPage(
            @PathVariable("provider") String provider
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(socialUtil.getLoginPageURL(provider)));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/members/login/social/{provider}/redirect")
    public ResponseEntity socialLogin(
            @PathVariable("provider") String provider,
            @RequestParam("code") String code) {

        return socialLoginService.execute(SocialLoginCode.builder().provider(provider).code(code).build());


    }
}