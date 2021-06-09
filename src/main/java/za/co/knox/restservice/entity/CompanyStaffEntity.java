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
@Table(name = "company_staff")
public class CompanyStaffEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_staff_id")
    private int companyStaffId;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_mobile")
    private String userMobile;

    @Column(name = "id_number")
    private String idNumber;


}


