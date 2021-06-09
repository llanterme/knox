package za.co.knox.restservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.knox.restservice.domain.CompanyStaff;
import za.co.knox.restservice.domain.ReportScreeningDateCounts;
import za.co.knox.restservice.entity.ScreeningQuestionsEntity;
import za.co.knox.restservice.enums.KnoxResponseCodesEnum;
import za.co.knox.restservice.enums.KnoxServiceResponseCodesEnum;
import za.co.knox.restservice.exceptions.ApiAuthenticationException;
import za.co.knox.restservice.exceptions.KnoxEligibilityException;
import za.co.knox.restservice.exceptions.ServiceException;
import za.co.knox.restservice.provider.ApiAuthenticationProvider;
import za.co.knox.restservice.request.OtpRequest;
import za.co.knox.restservice.request.CompanyRegistrationRequest;
import za.co.knox.restservice.request.ScreeningRequest;
import za.co.knox.restservice.response.ErrorResponse;
import za.co.knox.restservice.response.OtpResponse;
import za.co.knox.restservice.response.CompanyRegistrationResponse;
import za.co.knox.restservice.response.ScreeningResponse;
import za.co.knox.restservice.service.*;
import za.co.knox.restservice.utils.LoggerUtil;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class KnoxController {


    private ApiAuthenticationProvider apiAuthenticationProvider;
    private ApiService apiService;
    private CompanyService companyService;
    private MessagingService messagingService;
    private QuestionsService questionsService;
    private ScreeningService screeningService;


    public KnoxController(ApiAuthenticationProvider apiAuthenticationProvider, ApiService apiService,
                          CompanyService companyService,
                          QuestionsService questionsService,
                          MessagingService messagingService,
                          ScreeningService screeningService) {


        this.apiService = apiService;
        this.apiAuthenticationProvider = apiAuthenticationProvider;
        this.companyService = companyService;
        this.messagingService = messagingService;
        this.questionsService = questionsService;
        this.screeningService = screeningService;
    }


    @RequestMapping(path = "/version", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getApiVersion(@RequestHeader(value = "Authorization") String authorization) {
        try {
            apiAuthenticationProvider.authenticateApi(authorization);
        } catch (ApiAuthenticationException apiAuthenticationException) {
            return buildApiAuthenticationExceptionResponse();
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiService.getVersion());
    }


    @PostMapping(value = "/authcompany")
    public ResponseEntity authUser(@RequestHeader(value="Authorization") String authorization, @RequestBody CompanyRegistrationRequest companyRegistrationRequest) {

        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Authenticate user inbound payload:", companyRegistrationRequest));
        CompanyRegistrationResponse companyRegistrationResponse;

        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            companyRegistrationResponse = companyService.authenticateCompany(companyRegistrationRequest);
            log.info(loggerUtil.end("milestone: Authenticate user response", companyRegistrationResponse));
            return ResponseEntity.status(HttpStatus.OK).body(companyRegistrationResponse);
        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while authenticating user:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException digitalCowboyServiceException) {
            log.error(loggerUtil.error("milestone: Service error while authenticating user:", digitalCowboyServiceException.getResponseCodesEnum()));
            return buildKnoxServiceExceptionResponse(digitalCowboyServiceException.getResponseCodesEnum());
        } catch (KnoxEligibilityException knoxEligibilityException) {
            log.error(loggerUtil.error("milestone: Eligibility error while authenticating user:", knoxEligibilityException.getErrorMessage()));
            return buildEligibilityResponseExceptionResponse(knoxEligibilityException);
        }
    }


    @PostMapping(value = "/updateCompany")
    public ResponseEntity updateCompany(@RequestHeader(value="Authorization") String authorization, @RequestBody CompanyRegistrationRequest companyRegistrationRequest) {

        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Authenticate user inbound payload:", companyRegistrationRequest));
        CompanyRegistrationResponse companyRegistrationResponse;
              try {
            apiAuthenticationProvider.authenticateApi(authorization);
            companyRegistrationResponse = companyService.updateCompany(companyRegistrationRequest);
            return ResponseEntity.status(HttpStatus.OK).body(companyRegistrationRequest);
        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while authenticating user:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException digitalCowboyServiceException) {
            log.error(loggerUtil.error("milestone: Service error while authenticating user:", digitalCowboyServiceException.getResponseCodesEnum()));
            return buildKnoxServiceExceptionResponse(digitalCowboyServiceException.getResponseCodesEnum());
        } catch (KnoxEligibilityException knoxEligibilityException) {
            log.error(loggerUtil.error("milestone: Eligibility error while authenticating user:", knoxEligibilityException.getErrorMessage()));
            return buildEligibilityResponseExceptionResponse(knoxEligibilityException);
        }

    }



    @PostMapping(value = "/addscreening")
    public ResponseEntity addScreening(@RequestHeader(value="Authorization") String authorization, @RequestBody ScreeningRequest screeningRequest) {

        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Screening Request inbound payload:", screeningRequest));
        ScreeningResponse screeningResponse;

        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            screeningResponse = screeningService.addScreening(screeningRequest);
            log.info(loggerUtil.end("milestone: Add screening response", screeningResponse));

            return ResponseEntity.status(HttpStatus.OK).body(screeningResponse);

        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error during add screening:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException knoxServiceException) {
            log.error(loggerUtil.error("milestone: Service error during add screening:", knoxServiceException.getResponseCodesEnum()));
            return buildKnoxServiceExceptionResponse(knoxServiceException.getResponseCodesEnum());
        }

    }


    @RequestMapping(value = "/getcompanyscreeningsfordate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCompanyScreeningsForDate(@RequestHeader(value="Authorization") String authorization, @RequestParam String companyId, @RequestParam String screenDate) {
        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Get company screenings date:", screenDate));
        List<ScreeningRequest> screeningRequestsList;
        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            screeningRequestsList = screeningService.getCompanyScreeningsForDate(Integer.parseInt(companyId), screenDate);
            log.info(loggerUtil.end("milestone: Get screenings for company response", screeningRequestsList));
            return ResponseEntity.status(HttpStatus.OK).body(screeningRequestsList);

        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while getting screenings for company:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error getting screenings for company:", serviceException.getResponseCodesEnum()));
            return buildKnoxServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }

    @RequestMapping(value = "/passwordreminder", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendPasswordReminder(@RequestHeader(value="Authorization") String authorization, @RequestParam String mobileNumber) {
        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Get company password reminder for mobileNumber", mobileNumber));
        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            messagingService.sendPasswordReminder(mobileNumber);
            log.info(loggerUtil.end("milestone: Get company password reminder for mobileNumber", mobileNumber));
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while getting password reminder for mobileNumber", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error getting password reminder for mobileNumber:", serviceException.getResponseCodesEnum()));
            return buildKnoxServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }

    @RequestMapping(value = "/getcompanystaff", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCompanyStaff(@RequestHeader(value="Authorization") String authorization, @RequestParam String companyId) {
        LoggerUtil loggerUtil = new LoggerUtil();
        List<CompanyStaff> companyStaffList;
        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            companyStaffList = companyService.getCompanyStaff(Integer.parseInt(companyId));
            log.info(loggerUtil.end("milestone: Get company staff response", companyStaffList));
            return ResponseEntity.status(HttpStatus.OK).body(companyStaffList);

        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while getting company staff :", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error getting company staff :", serviceException.getResponseCodesEnum()));
            return buildKnoxServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }



    @RequestMapping(value = "/getscreeningquestions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getScreeningQuestions(@RequestHeader(value="Authorization") String authorization) {
        LoggerUtil loggerUtil = new LoggerUtil();
        List<ScreeningQuestionsEntity> screeningQuestionsEntityList;
        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            screeningQuestionsEntityList = questionsService.getScreeningQuestions();
            log.info(loggerUtil.end("milestone: Get screenings questions response", screeningQuestionsEntityList));
            return ResponseEntity.status(HttpStatus.OK).body(screeningQuestionsEntityList);

        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while getting screenings questions:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error getting screenings questions:", serviceException.getResponseCodesEnum()));
            return buildKnoxServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }

    @RequestMapping(value = "/getcompanyscreeningcounts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCompanyScreeningCounts(@RequestHeader(value="Authorization") String authorization,  @RequestParam String companyId) {
        LoggerUtil loggerUtil = new LoggerUtil();
        List<ReportScreeningDateCounts> screeningDatesList;
        try {
            log.info(loggerUtil.start("get screening dates inbound payload:", companyId));
            apiAuthenticationProvider.authenticateApi(authorization);
            screeningDatesList = screeningService.getCompanyScreeningsCounts(Integer.parseInt(companyId));
            log.info(loggerUtil.end("get screening dates inbound payload:", companyId));
            return ResponseEntity.status(HttpStatus.OK).body(screeningDatesList);

        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while getting screening dates for company:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error getting screening dates for company:", serviceException.getResponseCodesEnum()));
            return buildKnoxServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }

    }


    @PostMapping(value = "/registercompany")
    public ResponseEntity registerUCompany(@RequestHeader(value="Authorization") String authorization, @RequestBody CompanyRegistrationRequest companyRegistrationRequest) {

        LoggerUtil loggerUtil = new LoggerUtil();
        CompanyRegistrationResponse companyRegistrationResponse;
        log.info(loggerUtil.start("register company request inbound payload:", companyRegistrationRequest));

        try {
            apiAuthenticationProvider.authenticateApi(authorization);
            companyRegistrationResponse = companyService.registerCompany(companyRegistrationRequest);
            log.info(loggerUtil.end("milestone: register company response", companyRegistrationResponse));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(companyRegistrationResponse);
        } catch (KnoxEligibilityException knoxEligibilityException) {
            log.error(loggerUtil.error("milestone: Eligibility error while registering company:", knoxEligibilityException.getErrorMessage()));
            return buildEligibilityResponseExceptionResponse(knoxEligibilityException);
        }  catch (ApiAuthenticationException apiAuthenticationException) {

            log.error(loggerUtil.error("milestone: API Authentication error while registering company:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException serviceException) {
            log.error(loggerUtil.error("milestone: Service exception error while registering company:", serviceException.getResponseCodesEnum()));
            return buildKnoxServiceExceptionResponse(serviceException.getResponseCodesEnum());
        }



    }

    @RequestMapping(path = "/validateotp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity validateOtp(@RequestHeader(value = "Authorization") String authorization, @RequestBody OtpRequest otpRequest) {

        LoggerUtil loggerUtil = new LoggerUtil();
        OtpResponse otpResponse;
        log.info(loggerUtil.start("otp request inbound payload:", otpRequest));

        try {
            apiAuthenticationProvider.authenticateApi(authorization);

            otpResponse = messagingService.validateOtp(otpRequest);

            log.info(loggerUtil.end("milestone: otp response", otpResponse));
            return ResponseEntity.status(HttpStatus.OK).body(otpResponse);

        } catch (ApiAuthenticationException apiAuthenticationException) {
            log.error(loggerUtil.error("milestone: API Authentication error while validating otp:", apiAuthenticationException.getErrorMessage()));
            return buildApiAuthenticationExceptionResponse();
        } catch (ServiceException digitalCowboyServiceException) {
            log.error(loggerUtil.error("milestone: Service exception error while validating otp:", digitalCowboyServiceException.getResponseCodesEnum()));
            return buildKnoxServiceExceptionResponse(digitalCowboyServiceException.getResponseCodesEnum());
        } catch (KnoxEligibilityException knoxEligibilityException) {
            log.error(loggerUtil.error("milestone: Eligibility error while authenticating user:", knoxEligibilityException.getErrorMessage()));
            return buildEligibilityResponseExceptionResponse(knoxEligibilityException);
        }
    }




    private ResponseEntity<?> buildApiAuthenticationExceptionResponse() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResponseMessage(KnoxResponseCodesEnum.API_AUTHENTICATION_ERROR.getMessage());
        errorResponse.setResponseCode(KnoxResponseCodesEnum.API_AUTHENTICATION_ERROR.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private ResponseEntity<?> buildKnoxServiceExceptionResponse(KnoxServiceResponseCodesEnum knoxResponseCodesEnum) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResponseMessage(knoxResponseCodesEnum.getMessage());
        errorResponse.setResponseCode(knoxResponseCodesEnum.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private ResponseEntity<?> buildEligibilityResponseExceptionResponse(KnoxEligibilityException knoxEligibilityException) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResponseMessage(knoxEligibilityException.getErrorMessage());
        errorResponse.setResponseCode(Integer.parseInt(knoxEligibilityException.getErrorCode()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
