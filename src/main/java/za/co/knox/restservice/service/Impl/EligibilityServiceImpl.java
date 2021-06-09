package za.co.knox.restservice.service.Impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import za.co.knox.restservice.config.ConfigProperties;
import za.co.knox.restservice.enums.EligibilityReasonEnum;
import za.co.knox.restservice.exceptions.TokenEngineServiceException;
import za.co.knox.restservice.response.EligibilityResponse;
import za.co.knox.restservice.service.EligibilityService;
import za.co.knox.restservice.service.PropertyBag;
import za.co.knox.restservice.service.eligibility.CompanyLockedEligibilityCheck;
import za.co.knox.restservice.service.eligibility.DuplicateMobileEligibilityCheck;
import za.co.knox.restservice.service.eligibility.OtpValidEligibilityCheck;
import za.co.knox.restservice.service.eligibility.UserAuthenticationEligibilityCheck;
import za.co.knox.restservice.utils.LoggerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.function.Function;


@Slf4j
@Service
public class EligibilityServiceImpl implements EligibilityService {


    private ConfigProperties configProperties;

    private TaskExecutor taskExecutor;

    @Autowired
    public EligibilityServiceImpl(TaskExecutor taskExecutor,ConfigProperties configProperties) {
        this.taskExecutor = taskExecutor;
        this.configProperties = configProperties;
    }




    @Override
    public EligibilityResponse evaluateCompanyRegistrationEligibility(PropertyBag propertyBag)  {

        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("evaluateCompanyRegistrationEligibility", null));

        List<Function<PropertyBag, EligibilityResponse>> checks = new ArrayList<>();

        checks.add(DuplicateMobileEligibilityCheck.executeDuplicateMobileEligibilityCheck());


        EligibilityResponse eligibilityResponse = executeFutureTasks(checks, propertyBag);

        log.info(loggerUtil.end("evaluateCompanyRegistrationEligibility", eligibilityResponse));

        return eligibilityResponse;

    }


    @Override
    public EligibilityResponse evaluateCompanyAuthEligibility(PropertyBag propertyBag) throws TokenEngineServiceException {

        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("evaluateCompanyAuthEligibility", null));

        List<Function<PropertyBag, EligibilityResponse>> checks = new ArrayList<>();

        checks.add(UserAuthenticationEligibilityCheck.executeUserAuthenticationEligibilityCheck());
      //  checks.add(CompanyLockedEligibilityCheck.executeCompanyLockedEligibilityCheck());


        EligibilityResponse eligibilityResponse = executeFutureTasks(checks, propertyBag);

        log.info(loggerUtil.end("evaluateCompanyAuthEligibility", eligibilityResponse));

        return eligibilityResponse;

    }

    @Override
    public EligibilityResponse evaluateOtpValidityEligibility(PropertyBag propertyBag) throws TokenEngineServiceException {

        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("evaluateOtpValidityEligibility", null));

        List<Function<PropertyBag, EligibilityResponse>> checks = new ArrayList<>();

        checks.add(OtpValidEligibilityCheck.executeOtpValidEligibilityCheck(Integer.parseInt(configProperties.getOtpValidity())));

        EligibilityResponse eligibilityResponse = executeFutureTasks(checks, propertyBag);

        log.info(loggerUtil.end("evaluateOtpValidityEligibility", eligibilityResponse));

        return eligibilityResponse;

    }

    public EligibilityResponse executeFutureTasks(List<Function<PropertyBag, EligibilityResponse>> checks, PropertyBag propertyBag) {


        //TODO - Write all the decline reason to a database for reporting.
        List<FutureTask<EligibilityResponse>> checkFutureTasks = new ArrayList<>();
        List<EligibilityResponse> eligibilityResponses = new ArrayList<>();
        EligibilityResponse mainResponse;


        try {


            for (Function<PropertyBag, EligibilityResponse> check : checks) {
                FutureTask<EligibilityResponse> checkFutureTask = new FutureTask<>(() -> {
                    return check.apply(propertyBag);
                });

                checkFutureTasks.add(checkFutureTask);

                taskExecutor.execute(checkFutureTask);
            }

            for (FutureTask<EligibilityResponse> futureTask : checkFutureTasks) {
                try {

                    EligibilityResponse eligibilityCheckResponse = futureTask.get();
                    eligibilityResponses.add(eligibilityCheckResponse);


                } catch (Exception e) {

                    throw e;

                }


            }

            EligibilityResponse topResponse = eligibilityResponses.stream()
                    .filter(response -> response.getEligibilityReason() != EligibilityReasonEnum.SUCCESSFUL)
                    .sorted((response1, response2) -> {
                        return response1.getEligibilityReason().getPriority() - response2.getEligibilityReason().getPriority();
                    })
                    .findFirst()
                    .orElse(null);


            if (topResponse != null) {
                mainResponse = topResponse;
            } else {
                mainResponse = new EligibilityResponse();
                mainResponse.setEligibilityReason(EligibilityReasonEnum.SUCCESSFUL);
            }

        } catch (Exception e) {

            EligibilityResponse eligibilityResponse = new EligibilityResponse();
            eligibilityResponse.setEligibilityReason(EligibilityReasonEnum.UNHANDLED_ELIGIBILITY_EXCEPTION);

            return eligibilityResponse;
        }

        return mainResponse;

    }


}
