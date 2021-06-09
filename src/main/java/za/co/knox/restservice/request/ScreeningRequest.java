package za.co.knox.restservice.request;

import lombok.Getter;
import lombok.Setter;
import za.co.knox.restservice.domain.ScreeningQuestions;

import java.util.List;

@Getter
@Setter
public class ScreeningRequest {

    List<ScreeningQuestions> screeningQuestions;
    String userName;
    String userMobile;
    String idNumber;
    String userTemperature;
    String temperatureImageUrl;
    String staff;
    int companyId;
    int screeningId;


}
