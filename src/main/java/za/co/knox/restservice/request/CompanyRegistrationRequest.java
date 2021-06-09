package za.co.knox.restservice.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRegistrationRequest {

    private int companyId;
    private String companyIdentifier;
    private String mobileNumber;
    private String password;
    private String emailAddress;
    private String companyName;
    private String companyAddress;
    private String companyContactName;
}
