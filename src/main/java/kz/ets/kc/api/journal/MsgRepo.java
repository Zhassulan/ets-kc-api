package kz.ets.kc.api.journal;

import kz.ets.kc.api.journal.model.Msg;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MsgRepo extends JpaRepository <Msg, Long> {
}
