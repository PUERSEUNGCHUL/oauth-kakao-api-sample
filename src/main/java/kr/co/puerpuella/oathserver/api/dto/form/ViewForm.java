package kr.co.puerpuella.oathserver.api.dto.form;

import kr.co.puerpuella.oathserver.common.framework.CommonDTO;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ViewForm extends CommonDTO {

    private Long uid;
}
