package za.co.knox.restservice.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PinnedTopUpResponse extends BaseResponse {

    private String orderRef;
    
}
