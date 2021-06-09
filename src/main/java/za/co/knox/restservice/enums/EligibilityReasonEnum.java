package za.co.knox.restservice.enums;

import java.io.Serializable;
import java.util.Arrays;

public enum EligibilityReasonEnum implements Serializable {


    UNHANDLED_ELIGIBILITY_EXCEPTION("999", "Unhandled Exception on eligibility evaluation", 1),
    MOBILE_NUMBER_ALREADY_REGISTERED("02", "Mobile number already registered", 100),
    INVALID_USER_CREDENTIALS("02", "Invalid user authentication", 101),
    INVALID_OTP("04", "Invalid OTP", 103),
    OTP_EXPIRED("05", "OTP expired", 103),
    COMPANY_ACCOUNT_LOCKED("06", "Company account locked", 104),
    SUCCESSFUL("00", "Successful", 999);


    private final String reasonCode;
    private final String responseStatus;
    private final int priority;

    public static EligibilityReasonEnum findEligibilityReason(String reasonCode) {
        EligibilityReasonEnum result = (EligibilityReasonEnum) Arrays.stream(values()).filter((eligibilityReason) -> {
            return eligibilityReason.getReasonCode().equalsIgnoreCase(reasonCode) || eligibilityReason.getReasonCode().equalsIgnoreCase("0" + reasonCode);
        }).findFirst().orElse((null));
        return result;
    }

    public String getReasonCode() {
        return this.reasonCode;
    }

    public String getResponseStatus() {
        return this.responseStatus;
    }

    public int getPriority() {
        return this.priority;
    }

    private EligibilityReasonEnum(String reasonCode, String responseStatus, int priority) {
        this.reasonCode = reasonCode;
        this.responseStatus = responseStatus;
        this.priority = priority;
    }



}
