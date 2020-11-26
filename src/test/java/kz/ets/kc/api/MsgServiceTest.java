package kz.ets.kc.api;

import kz.ets.kc.api.data.MsgService;
import kz.ets.kc.api.data.model.Msg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MsgServiceTest {

    @Autowired
    private MsgService service;

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
        service.save(msg);
        assertTrue(service.findById(msg.getId()).isPresent());
    }
}
