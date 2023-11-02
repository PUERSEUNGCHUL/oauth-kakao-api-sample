package kr.co.puerpuella.oathserver.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.puerpuella.oathserver.api.dto.SocialLoginCode;
import kr.co.puerpuella.oathserver.api.service.SocialLoginService;
import kr.co.puerpuella.oathserver.api.util.SocialUtil;
import kr.co.puerpuella.oathserver.common.framework.CommonController;
import kr.co.puerpuella.oathserver.common.framework.response.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SocialController extends CommonController {

    private final SocialLoginService socialLoginService;


    @PostMapping("/members/login/social/{provider}/redirect")
    public ResponseBody socialLogin(
            HttpServletRequest request,
            @PathVariable("provider") String provider) {

        String code = request.getHeader("OpenAuthorization");

        SocialLoginCode codeDTO = SocialLoginCode.builder().code(code).provider(provider).code(code).build();
        return execute(socialLoginService, codeDTO);


    }
}