package kz.ets.kc.api.egov;

import kz.ets.kc.api.controller.model.CMSDataVerifyRequest;
import kz.gov.pki.kalkan.jce.provider.cms.CMSException;
import kz.gov.pki.kalkan.jce.provider.cms.CMSSignedData;

import java.io.IOException;

public interface KalkanService {

    CMSSignedData createCMSSignedData(String sigantureToVerify, String signedData) throws CMSException, IOException;

    void verifyCMSSignature(CMSDataVerifyRequest req) throws Exception;

    String certInfo(CMSDataVerifyRequest req) throws Exception;

}
