package kz.ets.kc.api.msg;

import kz.ets.kc.api.msg.model.Msg;
import kz.gov.pki.kalkan.jce.provider.cms.SignerInformationStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.cert.CertStore;
import java.util.Optional;

@Service
public class MsgService {

    @Autowired
    MsgRepo repo;

    public void save(Msg msg) {
        repo.save(msg);
    }

    public Optional<Msg> findById(Long id) {
        return repo.findById(id);
    }


}
