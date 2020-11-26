package kz.ets.kc.api.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class CMSDataVerifyResult {

    private int code;
    private String message;
    private String status;

}
