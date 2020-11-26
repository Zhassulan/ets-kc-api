package kz.ets.kc.api.kalkan;

import kz.ets.kc.api.data.MsgService;
import kz.ets.kc.api.data.model.Msg;
import kz.ets.kc.api.rest.model.CMSDataVerifyRequest;
import kz.gov.pki.kalkan.jce.provider.KalkanProvider;
import kz.gov.pki.kalkan.util.encoders.Base64;
import kz.gov.pki.provider.exception.ProviderUtilException;
import kz.gov.pki.provider.utils.CMSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Provider;
import java.security.Security;
import java.util.Date;

@Slf4j
@Service
public class KalkanService {

    private String providerName;
    private Provider provider;
    @Autowired
    private MsgService msgService;

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

    public void verifyCMSSignature(CMSDataVerifyRequest req) throws Exception {
        CMSUtil cmsUtil = new CMSUtil();
        try {
            cmsUtil.verifyCMS(Base64.decode(req.getSignedPlainData().getBytes()), Base64.decode(req.getPlainData().getBytes()), provider);
            msgService.save(new Msg(
                    null,
                    req.getLogin(),
                    new String(Base64.decode(req.getPlainData())),
                    req.getSignedPlainData(),
                    null,
                    new Date(),
                    new Date(),
                    true));
            log.debug("CMS signed data is verified.");
        } catch (ProviderUtilException e) {
            var msg = "Ошибка проверки подписанных CMS данных";
            log.error(msg, e);
            throw new Exception(msg);
        }
    }

}
