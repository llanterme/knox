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
@Table(name = "company")
public class CompanyEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private int companyId;

    @Column(name = "password")
    private String password;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "number_validate")
    private Long numberValidated;

    @Column(name = "account_locked")
    private String accountLocked;

    @Column(name = "company_address")
    private String companyAddress;

    @Column(name = "company_identifier")
    private String companyIdentifier;

    @Column(name = "company_contact_name")
    private String companyContactName;


}


