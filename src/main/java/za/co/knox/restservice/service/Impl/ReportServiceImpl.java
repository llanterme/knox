package za.co.knox.restservice.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import za.co.knox.restservice.entity.CompanyEntity;
import za.co.knox.restservice.entity.ScreeningEntity;
import za.co.knox.restservice.enums.KnoxServiceResponseCodesEnum;
import za.co.knox.restservice.exceptions.ServiceException;
import za.co.knox.restservice.repository.CompanyRepository;
import za.co.knox.restservice.repository.ScreeningRepository;
import za.co.knox.restservice.service.MessagingService;
import za.co.knox.restservice.service.ReportService;
import za.co.knox.restservice.utils.LoggerUtil;
import za.co.knox.restservice.utils.PdfUtils;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

   private ScreeningRepository screeningRepository;

   private CompanyRepository companyRepository;

   private MessagingService messagingService;

    @Autowired
    public ReportServiceImpl(ScreeningRepository screeningRepository,CompanyRepository companyRepository,MessagingService messagingService) {
        this.screeningRepository = screeningRepository;
        this.companyRepository = companyRepository;
        this.messagingService = messagingService;

    }

    @Override
    public void generateDailyVisitorReports(String date) throws ServiceException {
        List<CompanyEntity> companies = getAllCompaniesOptedOnForReports();

        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Starting ge for generate daily visitor reporting ", null));
        try {
            for (CompanyEntity item: companies) {

                // get all screenings for the day
                List<ScreeningEntity> screeningEntityList =  screeningRepository.findScreeningsByDateAndCompanyID(item.getCompanyId(),date);
                PdfUtils.generatePdf(screeningEntityList);

                log.info(loggerUtil.info("Sending email for company email address ", item.getEmailAddress()));
              //  messagingService.sendEmail("Test Content", "llanterme@gmail.com", "Daily Visitors Report");
            }
        } catch (Exception e) {
            //throw new ServiceException(e.getResponseCodesEnum());
        }
    }

    List<CompanyEntity> getAllCompaniesOptedOnForReports() throws ServiceException {
        List<CompanyEntity> companyEntityList = new ArrayList<>();
        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Starting get all companies for daily reporting ", null));
        try {
            companyRepository.findAll().forEach(companyEntityList::add);
            log.info(loggerUtil.info("Fetch all companies for daily report. Count is", companyEntityList.size()));
            return companyEntityList;

        } catch (DataAccessException e) {
            log.error(loggerUtil.error("Error while getting all companies for daily report ", e.getMessage()));
            throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_GET_COMPANIES_FOR_DAILY_REPORT  );
        }
    }
}
