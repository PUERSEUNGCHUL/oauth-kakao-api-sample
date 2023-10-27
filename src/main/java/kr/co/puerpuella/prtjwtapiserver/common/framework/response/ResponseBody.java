package kr.co.puerpuella.prtjwtapiserver.common.framework.response;

import kr.co.puerpuella.prtjwtapiserver.common.enums.ErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 모든 RestController의 요청에 대한 응답의 최상위 껍데기
 * status는 0,1,2로 구분
 *   0 : OK
 *   1 : NG
 *   2 : Warning (OK와 같지만, 에러정보가 존재)
 * responseData
 *   Front단에 전달해야할 내용 (status가 1인 경우는 null/ status가 정상인 경우에도  null인 경우 존재)
 * errorInfo
 *   Front단에 전달할 에러정보 (치명적인 에러나 경고포함)
 */
@AllArgsConstructor
@Builder
@Getter
public class ResponseBody {

    private String status;
    private CommonReturnData responseData;
    private ErrorInfo errorInfo;

}