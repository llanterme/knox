package za.co.knox.restservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyStaff {

    private int companyStaffId;
    private int companyId;
    private String userName;
    private String userMobile;
    private String IdNumber;


}
