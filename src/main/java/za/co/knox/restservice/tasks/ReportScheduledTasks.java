//package za.co.knox.restservice.tasks;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import za.co.knox.restservice.exceptions.ServiceException;
//import za.co.knox.restservice.service.ReportService;
//import za.co.knox.restservice.utils.LoggerUtil;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Component
//@Slf4j
//public class ReportScheduledTasks {
//
//    private ReportService reportingService;
//
//    @Autowired
//    ReportScheduledTasks(ReportService reportingService) {
//        this.reportingService = reportingService;
//    }
//
//    @Scheduled(fixedRate = 10000)
////    @Scheduled(cron="0 00 * * *")
//    public void executeDailyVisitorsReport() {
//        LoggerUtil loggerUtil = new LoggerUtil();
//
////        try {
////           String now =  new SimpleDateFormat("yyyy-MM-dd").format(new Date());
////            log.info(loggerUtil.start("Staring executeDailyVisitorsReport ", null));
////            reportingService.generateDailyVisitorReports(now);
////            log.info(loggerUtil.end("Ending executeDailyVisitorsReport", null));
////        } catch (ServiceException e) {
////            log.error(loggerUtil.error("Exception during executeDailyVisitorsReport ", e.getMessage()));
////        }
//
//    }
//}
