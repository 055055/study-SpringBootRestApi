package com.example.studyrestapi.demo.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode( of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {
    @Id @GeneratedValue
    private Integer id;

    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER) //여러개의 enum을 가질 수 있다.
    @Enumerated(value = EnumType.STRING)
    private Set<AccountRole> roles;


}
