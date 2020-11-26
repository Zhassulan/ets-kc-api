package kz.ets.kc.api.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Msg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    Long id;
    String login;
    String msg;
    String signature;
    String certinfo;
    Date created;
    Date modified;
    @Column(name = "check_status")
    Boolean status;

}
