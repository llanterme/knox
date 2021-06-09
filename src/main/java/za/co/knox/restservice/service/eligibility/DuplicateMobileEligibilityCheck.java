package za.co.knox.restservice.service.eligibility;


import lombok.extern.slf4j.Slf4j;
import za.co.knox.restservice.enums.EligibilityReasonEnum;
import za.co.knox.restservice.response.EligibilityResponse;
import za.co.knox.restservice.service.PropertyBag;

import java.util.function.Function;

/*
This check ensures the requesting product is active (1) in the DB and allowed to generate a token for.
*/

@Slf4j
public class DuplicateMobileEligibilityCheck {

    public static Function<PropertyBag, EligibilityResponse> executeDuplicateMobileEligibilityCheck(){

        return (propertyBag) -> {

            try {

                EligibilityResponse eligibilityResponse = new EligibilityResponse();


            if(!propertyBag.getCompanyRepository().validateUserAlreadyRegistered(propertyBag.getCompanyRegistrationRequest().getMobileNumber())) {
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.SUCCESSFUL);
            } else  {

                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.MOBILE_NUMBER_ALREADY_REGISTERED);
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
