package kr.co.puerpuella.oathserver.security;


import jakarta.servlet.http.HttpServletResponse;
import kr.co.puerpuella.oathserver.common.enums.ErrorInfo;
import kr.co.puerpuella.oathserver.common.framework.exception.ValidationException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@NoArgsConstructor
public class SecurityUtil {

    public static Long getCurrentUserId() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("Security Context에 인증정보가 없습니다.");
            throw new ValidationException(ErrorInfo.SYSTEM_ERROR);
        }

        String uid = null;

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            uid = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            uid = (String) authentication.getPrincipal();
        }

        try {
            String uidStr = Optional.ofNullable(uid).orElseThrow(()->new ValidationException(ErrorInfo.SYSTEM_ERROR));

            return Long.parseLong(uidStr);
        } catch (NumberFormatException e) {
            throw new ValidationException(ErrorInfo.SYSTEM_ERROR);
        }
    }

    public static void setResponse(HttpServletResponse response, ErrorInfo errorInfo) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorInfo.errorCode);
        response.getWriter().println("{ \"responseData\" : {},"
                + "\"responseInfo\" : {"
                + "\"responseCode\" : " + errorInfo.errorCode + ","
                + "\"responseMsg\" : \"" + errorInfo.errorMessage + "\""
                + "}");
    }

    public static String getUserIP() {

        return "127.0.0.1";
    }
}
