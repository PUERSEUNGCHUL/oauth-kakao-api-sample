package kr.co.puerpuella.oathserver.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestDTO {

    private String id;

    private String password;

    private String content;
}
