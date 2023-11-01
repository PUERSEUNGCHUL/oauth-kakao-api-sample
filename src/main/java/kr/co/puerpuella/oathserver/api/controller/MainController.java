package kr.co.puerpuella.oathserver.api.controller;

import kr.co.puerpuella.oathserver.api.dto.TestDTO;
import kr.co.puerpuella.oathserver.api.dto.form.JoinForm;
import kr.co.puerpuella.oathserver.api.dto.form.LoginForm;
import kr.co.puerpuella.oathserver.api.service.KakaoLoginService;
import kr.co.puerpuella.oathserver.api.service.LoginService;
import kr.co.puerpuella.oathserver.api.service.MemberJoinService;
import kr.co.puerpuella.oathserver.common.framework.CommonController;
import kr.co.puerpuella.oathserver.common.framework.response.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController extends CommonController {

    private final LoginService loginService;

    private final MemberJoinService joinService;

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/")
    public TestDTO main() {

        return TestDTO.builder()
                .id("1")
                .password("2")
                .build();

    }

    @GetMapping("/main")
    public TestDTO main2() {

        return TestDTO.builder()
                .id("2")
                .password("3")
                .build();
    }

    @PostMapping("/members/login")
    public ResponseBody login(@RequestBody LoginForm loginForm) {
        return execute(loginService, loginForm);
    }

    @PostMapping("/members")
    public ResponseBody join(@RequestBody JoinForm joginForm) {
        return execute(joinService, joginForm);
    }

    @PostMapping("/members/login/oauth")
    public ResponseBody oauth() {
        return execute(kakaoLoginService);
    }

}
