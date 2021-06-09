package za.co.knox.restservice.response;



import lombok.Getter;
import lombok.Setter;
import za.co.knox.restservice.enums.EligibilityReasonEnum;

@Getter
@Setter
public class EligibilityResponse {


    private EligibilityReasonEnum eligibilityReason;


}
