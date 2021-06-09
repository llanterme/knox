package za.co.knox.restservice.domain;

import lombok.Getter;
import lombok.Setter;
import za.co.knox.restservice.request.ScreeningRequest;

import java.util.List;

@Getter
@Setter
public class ReportScreeningDateCounts {
    private int companyId;
    private String screeningDate;
    private int screeningCount;

}
