package kz.ets.kc.api.rest;

import kz.ets.kc.api.kalkan.KalkanService;
import kz.ets.kc.api.rest.model.CMSDataVerifyRequest;
import kz.ets.kc.api.rest.model.CMSDataVerifyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kalkan", produces = "application/json;charset=UTF-8")
@Slf4j
public class KalkanController {

    @Autowired
    private KalkanService kalkanService;

    @PostMapping("/cms/verify")
    public ResponseEntity verify(@RequestBody CMSDataVerifyRequest req) {
        try {
            kalkanService.verifyCMSSignature(req);
            kalkanService.saveMsg(req);
            return new ResponseEntity(new CMSDataVerifyResponse(
                    1,
                    "Подпись корректна",
                    "SIGN OK"), HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR, request: " + req.toString(), e);
            return new ResponseEntity(new CMSDataVerifyResponse(
                    0,
                    e.getMessage(),
                    "BAD SIGN"), HttpStatus.BAD_REQUEST);
        }
    }

}
