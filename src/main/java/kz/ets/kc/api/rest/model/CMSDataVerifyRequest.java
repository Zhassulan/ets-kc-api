package kz.ets.kc.api.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class CMSDataVerifyRequest {

    private String login;
    private String plainData;
    private String signedPlainData;

    public CMSDataVerifyRequest() {
    }

    public CMSDataVerifyRequest(String login, String plainData, String signedPlainData) {
        this.login = login;
        this.plainData = plainData;
        this.signedPlainData = signedPlainData;
    }
}
