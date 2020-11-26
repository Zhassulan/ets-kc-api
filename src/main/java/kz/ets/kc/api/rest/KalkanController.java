package kz.ets.kc.api.rest;

import kz.ets.kc.api.kalkan.KalkanService;
import kz.ets.kc.api.rest.model.CMSDataVerifyRequest;
import kz.gov.pki.kalkan.util.encoders.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/kalkan", produces = "application/json;charset=UTF-8")
@Slf4j
public class KalkanController {

    @Autowired
    private KalkanService kalkanService;

    @PostMapping("/cms/verify")
    public ResponseEntity verify(@RequestBody CMSDataVerifyRequest req) {
        try {
            var res = kalkanService.verifyCMSSignature(req);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR: ", e);
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
