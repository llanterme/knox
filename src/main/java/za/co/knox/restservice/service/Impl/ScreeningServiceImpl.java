package za.co.knox.restservice.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import za.co.knox.restservice.domain.CompanyStaff;
import za.co.knox.restservice.domain.ReportScreeningDateCounts;
import za.co.knox.restservice.domain.ScreeningQuestions;
import za.co.knox.restservice.entity.ScreeningEntity;
import za.co.knox.restservice.enums.KnoxServiceResponseCodesEnum;
import za.co.knox.restservice.exceptions.ServiceException;
import za.co.knox.restservice.repository.ScreeningRepository;
import za.co.knox.restservice.request.ScreeningRequest;
import za.co.knox.restservice.response.ScreeningResponse;
import za.co.knox.restservice.service.CompanyService;
import za.co.knox.restservice.service.ScreeningService;
import za.co.knox.restservice.utils.LoggerUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static za.co.knox.restservice.utils.Utils.fromJSON;


@Service
@Slf4j
public class ScreeningServiceImpl implements ScreeningService {

   private ScreeningRepository screeningRepository;

   private CompanyService companyService;

    @Autowired
    public ScreeningServiceImpl(ScreeningRepository screeningRepository, CompanyService companyService) {
        this.screeningRepository = screeningRepository;
        this.companyService = companyService;
    }

    @Override
    public ScreeningResponse addScreening(ScreeningRequest screeningRequest) throws ServiceException {

        LoggerUtil loggerUtil = new LoggerUtil();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ScreeningResponse screeningResponse  = new ScreeningResponse();

        ScreeningEntity screeningEntity = new ScreeningEntity();
        screeningEntity.setScreenDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        screeningEntity.setScreenTime(new SimpleDateFormat("hh:mm:ss").format(new Date()));
        screeningEntity.setCompanyId(screeningRequest.getCompanyId());
        screeningEntity.setUserName(screeningRequest.getUserName());
        screeningEntity.setUserMobile(screeningRequest.getUserMobile());
        screeningEntity.setUserTemperature(screeningRequest.getUserTemperature());
        screeningEntity.setTemperatureImageUrl(screeningRequest.getTemperatureImageUrl());
        screeningEntity.setScreeningQuestions(gson.toJson(screeningRequest.getScreeningQuestions()));
        screeningEntity.setStaff(screeningRequest.getStaff());
        screeningEntity.setIdNumber(screeningRequest.getIdNumber());

        try {
            log.info(loggerUtil.start("stating add screening questions ", null));
            int companyId = screeningRepository.save(screeningEntity).getCompanyId();

            if(screeningRequest.getStaff().equalsIgnoreCase("Yes")) {
                CompanyStaff companyStaff = new CompanyStaff();
                companyStaff.setUserMobile(screeningRequest.getUserMobile());
                companyStaff.setUserName(screeningRequest.getUserName());
                companyStaff.setCompanyId(companyId);
                companyStaff.setIdNumber(screeningRequest.getIdNumber());
                companyService.addCompanyUser(companyStaff);
            }

            screeningResponse.setResponseCode(0);
            screeningResponse.setResponseMessage(KnoxServiceResponseCodesEnum.SUCCESSFUL.getMessage());
            log.info(loggerUtil.end("ending add screening questions", null));
            return screeningResponse;


        } catch (DataAccessException e) {
            throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_PERSIST_SCREENING_QUESTIONS  );
        }
        catch (ServiceException e) {
            throw new ServiceException(e.getResponseCodesEnum());
        }
    }

    @Override
    public List<ScreeningRequest> getCompanyScreeningsForDate(int companyId, String screenDate) throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();
        List<ScreeningRequest> screeningRequestList = new ArrayList<>();

        try {
            log.info(loggerUtil.start("stating get screenings  for company Id ", companyId));
            List<ScreeningEntity> screeningEntitiesList = screeningRepository.findScreeningsByDateAndCompanyID(companyId, screenDate);

             for(ScreeningEntity item: screeningEntitiesList) {

                List<ScreeningQuestions> screeningQuestions = fromJSON(new TypeReference<List<ScreeningQuestions>>() {}, item.getScreeningQuestions());

                ScreeningRequest aScreenRequest = new ScreeningRequest();
                aScreenRequest.setUserName(item.getUserName());
                aScreenRequest.setCompanyId(companyId);
                aScreenRequest.setScreeningId(item.getScreeningId());
                aScreenRequest.setTemperatureImageUrl(item.getTemperatureImageUrl());
                aScreenRequest.setUserMobile(item.getUserMobile());
                aScreenRequest.setUserTemperature(item.getUserTemperature());
                aScreenRequest.setIdNumber(item.getIdNumber());
                aScreenRequest.setScreeningQuestions(screeningQuestions);

                screeningRequestList.add(aScreenRequest);
            }


            log.info(loggerUtil.end("ending get screenings  for company Id ", companyId));
            return screeningRequestList;

        }  catch (Exception e) {
            log.error(loggerUtil.error("error getting screening dates and counts for company Id ", e.getMessage()));
            throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_GET_SCREENING_DATES  );
        }
    }

    @Override
    public List<ReportScreeningDateCounts> getCompanyScreeningsCounts(int companyId) throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();
        List<ReportScreeningDateCounts> screeningDatesList = new ArrayList<>();

        try {
            log.info(loggerUtil.start("stating get screening dates and counts for company Id ", companyId));

            List<Object[]> screeningEntityList = screeningRepository.findDistinctByScreenDate(companyId);

            for(Object[] item: screeningEntityList) {

                ReportScreeningDateCounts screeningDate = new ReportScreeningDateCounts();
                int count = screeningRepository.findCountByDateAndCompanyId(companyId, (item[0]).toString());
                screeningDate.setScreeningCount(count);
                screeningDate.setCompanyId(companyId);
                screeningDate.setScreeningDate((item[0]).toString());

                screeningDatesList.add(screeningDate);


            }
            screeningDatesList.sort(Comparator.comparing(ReportScreeningDateCounts::getScreeningDate).reversed());
            log.info(loggerUtil.start("end get screening dates and counts for company Id ", companyId));
            return screeningDatesList;

        } catch (Exception e) {
            log.error(loggerUtil.error("error getting screening dates and counts for company Id ", e.getMessage()));
            throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_GET_SCREENING_DATES  );
        }

    }


}
