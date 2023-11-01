
package kr.co.puerpuella.oathserver.common.framework.exception;

import kr.co.puerpuella.oathserver.common.enums.ErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationException extends RuntimeException{

    private ErrorInfo errorInfo;
}
