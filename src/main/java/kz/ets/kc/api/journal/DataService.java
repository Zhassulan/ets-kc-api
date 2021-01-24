package kz.ets.kc.api.journal;

import kz.ets.kc.api.controller.model.CMSDataVerifyRequest;
import kz.ets.kc.api.journal.model.Msg;

import java.util.Optional;

public interface DataService {

    void save(Msg msg);

    void save(CMSDataVerifyRequest req) throws Exception;

    Optional<Msg> findById(Long id);

}
