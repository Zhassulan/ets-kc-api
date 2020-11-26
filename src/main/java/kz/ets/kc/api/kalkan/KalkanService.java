package kz.ets.kc.api.kalkan;

import kz.gov.pki.kalkan.jce.provider.KalkanProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Provider;
import java.security.Security;

@Slf4j
@Service
public class KalkanService {

    private String providerName;

    public KalkanService() {
        Provider kalkanProvider = new KalkanProvider();
        boolean exists = false;
        Provider[] providers = Security.getProviders();
        for (Provider p : providers) {
            if (p.getName().equals(kalkanProvider.getName())) {
                exists = true;
            }
        }
        if (!exists) {
            Security.addProvider(kalkanProvider);
        }
        providerName = kalkanProvider.getName();
        log.debug("Kalkan provider name \"" + providerName + "\"");
    }
}
