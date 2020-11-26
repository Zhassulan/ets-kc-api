package kz.ets.kc.api.kalkan;

import kz.ets.kc.api.rest.model.CMSDataVerifyRequest;
import kz.gov.pki.kalkan.jce.provider.KalkanProvider;
import kz.gov.pki.kalkan.jce.provider.cms.CMSException;
import kz.gov.pki.kalkan.jce.provider.cms.SignerInformation;
import kz.gov.pki.kalkan.jce.provider.cms.SignerInformationStore;
import kz.gov.pki.kalkan.util.encoders.Base64;
import kz.gov.pki.provider.exception.ProviderUtilException;
import kz.gov.pki.provider.utils.CMSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.*;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Service
public class KalkanService {

    private String providerName;
    private Provider provider;

    public Provider getProvider() {
        return provider;
    }

    public KalkanService() {
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

    public Boolean verifyCMSSignature(CMSDataVerifyRequest req) throws Exception {
        var brokerCode = req.getLogin();
        CMSUtil cmsUtil = new CMSUtil();
        try {
            cmsUtil.verifyCMS(Base64.decode(req.getSignedPlainData().getBytes()), Base64.decode(req.getPlainData().getBytes()), provider);
            log.debug("CMS signed data is verified.");
            return true;
        } catch (ProviderUtilException e) {
            var msg = "Ошибка проверки подписанных CMS данных";
            log.error(msg, e);
            throw new Exception(msg);
        }
    }

    private String getCertInfo(X509Certificate userCert) {
        String info = "--------------Certificate information-----------------";
        info += userCert.getVersion() + "\n";
        info += userCert.getSerialNumber().toString(16) + "\n";
        info += userCert.getSubjectDN() + "\n";
        info += userCert.getIssuerDN() + "\n";
        info += userCert.getNotBefore() + "\n";
        info += userCert.getNotAfter() + "\n";
        info += userCert.getSigAlgName() + "\n";
        return info;
    }

    private boolean validateSignature(SignerInformationStore signers, CertStore clientCerts) throws Exception {
        Iterator it = signers.getSigners().iterator();
        String msg;
        boolean res = false;
        while (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();
            X509CertSelector signerConstraints = signer.getSID();
            Collection certCollection = clientCerts.getCertificates(signerConstraints);
            Iterator certIt = certCollection.iterator();
            int indexOfSigner = 0;
            while (certIt.hasNext()) {
                indexOfSigner++;
                X509Certificate cert = (X509Certificate) certIt.next();
                log.debug(getCertInfo(cert));
                //log.info("------ Сертификат внутри подписи: " + indexOfSigner+ " ----- ");
                //log.info(cert.toString());
                try {
                    //проверка сертификатом внутри подписи
                    cert.checkValidity();
                    res = signer.verify(cert, providerName);
                    //проверка сертификатом из базы ранее присланного
                    var b = new String("aaa").getBytes();
                    X509Certificate cert_db = getCertFromBytes(b); //todo get cert from db by broker
                    cert_db.checkValidity();
                    res = (res) && (signer.verify(cert_db, providerName));
                } catch (CertificateExpiredException ex) {
                    msg = "Сертификат просрочен";
                    log.error(msg, ex);
                    throw new Exception(msg);
                } catch (CertificateNotYetValidException ex) {
                    msg = "Сертификат не валиден";
                    log.error(msg, ex);
                    throw new Exception(msg);
                }
            }
            if (indexOfSigner == 0) {
                msg = "Данные подписаны, но проверочный сертификат не валиден";
                log.error(msg);
                throw new Exception(msg);
            }
            if (!res) {
                msg = "Ошибка проверки подписанных данных и ошибка сертификата";
                log.error(msg);
                throw new Exception(msg);
            }
        }
        return res;
    }

    public X509Certificate getCertFromBytes(byte[] cert) throws Exception {
        try {
            return (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(cert));
        } catch (Exception e) {
            var msg = "Ошибка получения сертификата из байтов";
            log.error(msg, e);
            throw new Exception(msg);
        }
    }

}
