package kz.ets.kc.api.journal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Msg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    private String login;
    private String msg;
    private String signature;
    private String certinfo;
    private Date created;
    private Date modified;
    @Column(name = "check_status")
    private Boolean status;

}
