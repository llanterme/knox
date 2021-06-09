package za.co.knox.restservice.service;


import za.co.knox.restservice.domain.ReportScreeningDateCounts;
import za.co.knox.restservice.exceptions.ServiceException;
import za.co.knox.restservice.request.ScreeningRequest;
import za.co.knox.restservice.response.ScreeningResponse;

import java.util.List;

public interface ReportService {

    void generateDailyVisitorReports(String date) throws ServiceException;

}
