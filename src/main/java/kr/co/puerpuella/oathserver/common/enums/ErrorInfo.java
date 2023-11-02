package kr.co.puerpuella.oathserver.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorInfo {

    JOIN_DUPLICATED_NICKNAME(1001, "이미 존재하는 닉네임입니다."),
    JOIN_DUPLICATED_EMAIL(1002, "이미 존재하는 메일입니다."),
    LOGIN_NO_EMAIL(2001, "가입되어 있지 않은 메일입니다."),
    LOGIN_INVALID_PASSWORD(2002, "패스워드가 일치하지 않습니다."),

    SECURITY_INVALID_AUTHORIZED(3001, "로그인되어있지 않습니다."),
    SECURITY_ACCESS_DENIED(3002,"액세스가 거부되었습니다."),
    SECURITY_INVALID_SIGN(3011,"잘못된 JWT 서명입니다."),
    SECURITY_EXPIRED_TOKEN(3012,"만료된 JWT 토큰입니다."),
    SECURITY_NO_SUPPORT_TOKEN(3013,"지원되지 않는 JWT토큰입니다."),
    SECURITY_INVALID_TOKEN(3014,"JWT 토큰이 잘못되었습니다."),
    SECURITY_NO_TOKEN(3015,"JWT 토큰이 존재하지 않습니다."),
    SECURITY_NO_MATCH_REFRESH_TOKEN(3020,"유효하지 않은 Refresh토큰입니다."),

    SYSTEM_ERROR(9999, "시스템에러")
    ;

    public int errorCode;
    public String errorMessage;
}
