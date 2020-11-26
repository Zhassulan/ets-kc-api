package kz.ets.kc.api.data;

import kz.ets.kc.api.data.model.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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