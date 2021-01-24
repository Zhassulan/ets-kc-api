package kz.ets.kc.api.journal;

import kz.ets.kc.api.controller.model.CMSDataVerifyRequest;
import kz.ets.kc.api.egov.KalkanService;
import kz.ets.kc.api.journal.model.Msg;
import kz.gov.pki.kalkan.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class DataServiceImpl implements DataService {

    @Autowired MsgRepo repo;
    private KalkanService kalkanService;

    @Autowired
    public void setKalkanService(KalkanService kalkanService) {
        this.kalkanService = kalkanService;
    }

    @Bean
    DataService dataService() {
        return new DataServiceImpl();
    }

    public void save(Msg msg) {
        repo.save(msg);
    }

    public Optional<Msg> findById(Long id) {
        return repo.findById(id);
    }

    public void save(CMSDataVerifyRequest req) throws Exception {
        save(new Msg(
                null,
                req.getLogin(),
                new String(Base64.decode(req.getPlainData())),
                req.getSignedPlainData(),
                kalkanService.certInfo(req),
                new Date(),
                new Date(),
                true));
    }
}
