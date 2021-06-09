package za.co.knox.restservice.service.eligibility;


import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import za.co.knox.restservice.entity.CompanyEntity;
import za.co.knox.restservice.enums.EligibilityReasonEnum;
import za.co.knox.restservice.response.CompanyRegistrationResponse;
import za.co.knox.restservice.response.EligibilityResponse;
import za.co.knox.restservice.service.PropertyBag;

import java.util.function.Function;

/*
This check ensures the requesting product is active (1) in the DB and allowed to generate a token for.
*/

@Slf4j
public class CompanyLockedEligibilityCheck {

    public static Function<PropertyBag, EligibilityResponse> executeCompanyLockedEligibilityCheck(){

        return (propertyBag) -> {

            try {

                EligibilityResponse eligibilityResponse = new EligibilityResponse();

                CompanyEntity companyEntity = propertyBag.getCompanyRepository().validateCompanyNotLocked(propertyBag.getCompanyRegistrationResponse().getCompanyId());

            if(companyEntity != null) {

                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.SUCCESSFUL);
            } else  {

                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.COMPANY_ACCOUNT_LOCKED);
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
