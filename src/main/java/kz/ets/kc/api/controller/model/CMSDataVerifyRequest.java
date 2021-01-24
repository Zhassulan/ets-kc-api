package kz.ets.kc.api.controller.model;

import lombok.Data;

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
