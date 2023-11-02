package kr.co.puerpuella.oathserver.api.controller;

import kr.co.puerpuella.oathserver.api.dto.MemberInfoDto;
import kr.co.puerpuella.oathserver.api.dto.TestDTO;
import kr.co.puerpuella.oathserver.api.dto.form.JoinForm;
import kr.co.puerpuella.oathserver.api.dto.form.LoginForm;
import kr.co.puerpuella.oathserver.api.dto.form.ViewForm;
import kr.co.puerpuella.oathserver.api.service.LoginService;
import kr.co.puerpuella.oathserver.api.service.MemberJoinService;
import kr.co.puerpuella.oathserver.api.service.MemberViewService;
import kr.co.puerpuella.oathserver.common.framework.CommonController;
import kr.co.puerpuella.oathserver.common.framework.response.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MainController extends CommonController {

    private final LoginService loginService;

    private final MemberJoinService joinService;
    private final MemberViewService memberViewService;

    @PostMapping("/members/login")
    public ResponseBody login(@RequestBody LoginForm loginForm) {
        return execute(loginService, loginForm);
    }

    @PostMapping("/members")
    public ResponseBody join(@RequestBody JoinForm joginForm) {
        return execute(joinService, joginForm);
    }

    @GetMapping("/members/{uid}")
    public ResponseBody viewMember(@PathVariable("uid") Long uid) {
        return execute(memberViewService, ViewForm.builder().uid(uid).build());
    }




}
