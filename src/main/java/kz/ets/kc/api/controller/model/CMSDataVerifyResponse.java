package kz.ets.kc.api.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CMSDataVerifyResponse {

    private int code;
    private String message;
    private String status;

}
