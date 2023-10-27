package kr.co.puerpuella.prtjwtapiserver.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class TestDTO {

    private String id;

    private String password;

    private String content;
}
