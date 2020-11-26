package kz.ets.kc.api;

import kz.ets.kc.api.model.Msg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MsgRepo extends JpaRepository <Msg, Long> {
}
