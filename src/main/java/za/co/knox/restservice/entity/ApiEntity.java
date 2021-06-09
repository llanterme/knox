package za.co.knox.restservice.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "api")
public class ApiEntity {

    @Id
    @Setter
    @GeneratedValue
    @Column(name = "api_id")
    private Integer apiId;

    @Column(name = "api_version")
    private String apiVersion;
}


