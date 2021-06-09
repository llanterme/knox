package za.co.knox.restservice.service.eligibility;


import lombok.extern.slf4j.Slf4j;
import za.co.knox.restservice.entity.OtpEntity;
import za.co.knox.restservice.enums.EligibilityReasonEnum;
import za.co.knox.restservice.response.EligibilityResponse;
import za.co.knox.restservice.service.PropertyBag;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;


@Slf4j
public class OtpValidEligibilityCheck {

    public static Function<PropertyBag, EligibilityResponse> executeOtpValidEligibilityCheck(int otpValidityPeriod ){

        return (propertyBag) -> {

            OtpEntity otpEntity;
            try {

                EligibilityResponse eligibilityResponse = new EligibilityResponse();

                 otpEntity = propertyBag.getOtpRepository().validateOtp(propertyBag.getOtpRequest().getCompanyId(), propertyBag.getOtpRequest().getCode());

            if(otpEntity == null) {
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.INVALID_OTP);
            } else if (!validateOtpTime(otpEntity, otpValidityPeriod))  {

                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.OTP_EXPIRED);
            } else {
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.SUCCESSFUL);
            }
                return eligibilityResponse;
            }catch (Exception e) {

                EligibilityResponse eligibilityResponse = new EligibilityResponse();
                eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.UNHANDLED_ELIGIBILITY_EXCEPTION);
                return eligibilityResponse;
            }

        };

    }

    private static boolean validateOtpTime(OtpEntity otpEntity, int otpValidityPeriod) {

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            Date date = new Date();

            String currentTime = sdf.format(date.getTime());
            Date storedTime = sdf.parse(otpEntity.getTimeReceived());


            long milliseconds = sdf.parse(currentTime).getTime() - storedTime.getTime();
            int seconds = (int) milliseconds / 1000;
            int minutes = (seconds % 3600) / 60;


            if(minutes > otpValidityPeriod) {

                return false;
            }

            else  {
                return true;
            }


        } catch (Exception e){
            return false;
        }

    }



}
