package kz.ets.kc.api.egov;

import kz.ets.kc.api.journal.DataService;
import kz.ets.kc.api.journal.DataServiceImpl;
import kz.ets.kc.api.journal.model.Msg;
import kz.ets.kc.api.controller.model.CMSDataVerifyRequest;
import kz.gov.pki.kalkan.jce.provider.KalkanProvider;
import kz.gov.pki.kalkan.jce.provider.cms.*;
import kz.gov.pki.kalkan.util.encoders.Base64;
import kz.gov.pki.provider.exception.ProviderUtilException;
import kz.gov.pki.provider.utils.CMSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Slf4j
@Service
public class KalkanServiceImpl implements KalkanService {

    private String providerName;
    private Provider provider;

    @Bean
    public KalkanService kalkanService() {
        return new KalkanServiceImpl();
    }

    public KalkanServiceImpl() {
        provider = new KalkanProvider();
        boolean exists = false;
        Provider[] providers = Security.getProviders();
        for (Provider p : providers) {
            if (p.getName().equals(provider.getName())) {
                exists = true;
            }
        }
        if (!exists) {
            Security.addProvider(provider);
        }
        providerName = provider.getName();
        log.debug("Kalkan provider name \"" + providerName + "\"");
    }

    public void verifyCMSSignature(CMSDataVerifyRequest req) throws Exception {
        CMSUtil cmsUtil = new CMSUtil();
        try {
            cmsUtil.verifyCMS(Base64.decode(req.getSignedPlainData().getBytes()), Base64.decode(req.getPlainData().getBytes()), provider);
            log.debug("CMS signed data is verified.");
        } catch (ProviderUtilException e) {
            var msg = "Ошибка проверки подписанных CMS данных";
            log.error(msg, e);
            throw new Exception(msg);
        }
    }

    public String certInfo(CMSDataVerifyRequest req) throws Exception {
        String certInfo = null;
        CMSSignedData cms = createCMSSignedData(req.getSignedPlainData(), req.getPlainData());
        SignerInformationStore signers = cms.getSignerInfos();
        CertStore clientCerts = cms.getCertificatesAndCRLs("Collection", providerName);
        Iterator it = signers.getSigners().iterator();
        while (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();
            X509CertSelector signerConstraints = signer.getSID();
            Collection certCollection = clientCerts.getCertificates(signerConstraints);
            Iterator certIt = certCollection.iterator();
            while (certIt.hasNext()) {
                X509Certificate cert = (X509Certificate) certIt.next();
                certInfo = certInfoString(cert);
                log.debug(certInfo);
            }
        }
        return certInfo;
    }

    private String certInfoString(X509Certificate userCert) {
        String info = null;
        info = "--------------Certificate information-----------------" + "\n";
        info += "Версия: " + userCert.getVersion() + "\n";
        info += "Серийный номер: " + userCert.getSerialNumber().toString(16) + "\n";
        info += "Пользователь/Организация: " + userCert.getSubjectDN() + "\n";
        info += "Издатель сертификата: " + userCert.getIssuerDN() + "\n";
        info += "Сертификат действителен с: " + userCert.getNotBefore() + "\n";
        info += "Сертификат действителен до: " + userCert.getNotAfter() + "\n";
        info += "Алгоритм шифрования: " + userCert.getSigAlgName() + "\n";
        info += "--------------Certificate information-----------------";
        return info;
    }

    public CMSSignedData createCMSSignedData(String sigantureToVerify, String signedData) throws CMSException, IOException {
        CMSSignedData cms = new CMSSignedData(Base64.decode(sigantureToVerify));
        boolean isAttachedContent = cms.getSignedContent() != null;
        if (isAttachedContent) {
            cms = new CMSSignedData(cms.getEncoded());
        } else {
            CMSProcessableByteArray data = new CMSProcessableByteArray(signedData.getBytes("UTF-8"));
            cms = new CMSSignedData(data, cms.getEncoded());
        }
        return cms;
    }

}
