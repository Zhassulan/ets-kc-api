package kz.ets.kc.api;

import kz.ets.kc.api.journal.MsgRepo;
import kz.ets.kc.api.journal.model.Msg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class DataServiceImplTest {

    @Autowired
    public MsgRepo repo;

    @Test
    public void save() {
        var msg = new Msg(null,
                "test",
                "message",
                "signature",
                "cert info",
                new Date(),
                new Date(),
                true);
        repo.save(msg);
        assertTrue(repo.findById(msg.getId()).isPresent());
    }
}
