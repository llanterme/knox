package za.co.knox.restservice.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PinnedTopUpRequest {

    private String recipientMsisdn;
    private int voucherId;
    private int userId;
    private BigDecimal amount;
    private String voucherDescription;

}
