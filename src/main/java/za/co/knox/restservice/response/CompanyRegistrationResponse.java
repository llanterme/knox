package za.co.knox.restservice.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegistrationResponse extends BaseResponse {
    private int companyId;
    private String companyIdentifier;
    private String mobileNumber;
    private String password;
    private String emailAddress;
    private String companyName;
    private String companyAddress;
    private String companyContactName;
}
