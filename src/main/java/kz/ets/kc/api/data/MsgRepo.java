package kz.ets.kc.api.data;

import kz.ets.kc.api.data.model.Msg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MsgRepo extends JpaRepository <Msg, Long> {
}
