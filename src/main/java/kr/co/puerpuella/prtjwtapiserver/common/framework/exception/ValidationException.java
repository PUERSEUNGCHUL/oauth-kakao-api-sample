
package kr.co.puerpuella.prtjwtapiserver.common.framework.exception;

import kr.co.puerpuella.prtjwtapiserver.common.enums.ErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationException extends RuntimeException{

    private ErrorInfo errorInfo;
}
