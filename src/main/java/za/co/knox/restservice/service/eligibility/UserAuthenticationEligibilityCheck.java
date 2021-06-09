package za.co.knox.restservice.service.eligibility;


import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import za.co.knox.restservice.config.ConfigProperties;
import za.co.knox.restservice.entity.CompanyEntity;
import za.co.knox.restservice.enums.EligibilityReasonEnum;
import za.co.knox.restservice.response.EligibilityResponse;
import za.co.knox.restservice.response.CompanyRegistrationResponse;
import za.co.knox.restservice.service.PropertyBag;
import za.co.knox.restservice.utils.AES;

import java.util.function.Function;

/*
This check ensures the requesting product is active (1) in the DB and allowed to generate a token for.
*/

@Slf4j
public class UserAuthenticationEligibilityCheck {
    private ConfigProperties configProperties;

    public static Function<PropertyBag, EligibilityResponse> executeUserAuthenticationEligibilityCheck(){

        return (propertyBag) -> {

            try {

                EligibilityResponse eligibilityResponse = new EligibilityResponse();

                String encryptedPassword = AES.encrypt(propertyBag.getCompanyRegistrationRequest().getPassword(), propertyBag.getConfigProperties().getSecretKey());

                CompanyEntity companyEntity = propertyBag.getCompanyRepository().authenticateUser(propertyBag.getCompanyRegistrationRequest().getMobileNumber(),encryptedPassword);

            if(companyEntity != null) {

                if (companyEntity.getAccountLocked().equalsIgnoreCase("1")) {
                    eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.COMPANY_ACCOUNT_LOCKED);
                } else {

                    CompanyRegistrationResponse companyRegistrationResponse = new DozerBeanMapper().map(companyEntity, CompanyRegistrationResponse.class);
                    companyRegistrationResponse.setEmailAddress(companyEntity.getEmailAddress());
                    companyRegistrationResponse.setCompanyName(companyEntity.getCompanyName());
                    companyRegistrationResponse.setCompanyId(companyEntity.getCompanyId());
                    companyRegistrationResponse.setCompanyAddress(companyEntity.getCompanyAddress());
                    companyRegistrationResponse.setPassword(AES.decrypt(companyEntity.getPassword(), propertyBag.getConfigProperties().getSecretKey()));


                    propertyBag.setCompanyRegistrationResponse(companyRegistrationResponse);

                    eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.SUCCESSFUL);
                }
            } else {
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.INVALID_USER_CREDENTIALS);
            }
                return eligibilityResponse;
            }catch (Exception e) {

                EligibilityResponse eligibilityResponse = new EligibilityResponse();
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.UNHANDLED_ELIGIBILITY_EXCEPTION);
                return eligibilityResponse;
            }

        };

    }



}
