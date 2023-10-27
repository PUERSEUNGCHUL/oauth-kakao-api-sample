package kr.co.puerpuella.prtjwtapiserver.api.controller;

import kr.co.puerpuella.prtjwtapiserver.api.dto.TestDTO;
import kr.co.puerpuella.prtjwtapiserver.api.dto.form.JoinForm;
import kr.co.puerpuella.prtjwtapiserver.api.dto.form.LoginForm;
import kr.co.puerpuella.prtjwtapiserver.api.service.LoginService;
import kr.co.puerpuella.prtjwtapiserver.api.service.MemberJoinService;
import kr.co.puerpuella.prtjwtapiserver.common.framework.CommonController;
import kr.co.puerpuella.prtjwtapiserver.common.framework.CommonService;
import kr.co.puerpuella.prtjwtapiserver.common.framework.response.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController extends CommonController {

    private final LoginService loginService;

    private final MemberJoinService joinService;

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

}
