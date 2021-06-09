package za.co.knox.restservice.service;


import za.co.knox.restservice.domain.ReportScreeningDateCounts;
import za.co.knox.restservice.exceptions.ServiceException;
import za.co.knox.restservice.request.ScreeningRequest;
import za.co.knox.restservice.response.ScreeningResponse;

import javax.xml.ws.Service;
import java.util.List;

public interface ScreeningService {

    ScreeningResponse addScreening(ScreeningRequest screeningRequest) throws ServiceException;

    List<ReportScreeningDateCounts> getCompanyScreeningsCounts(int companyId) throws ServiceException;

    List<ScreeningRequest> getCompanyScreeningsForDate(int companyId, String screenDate) throws ServiceException;






}
