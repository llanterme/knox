package za.co.knox.restservice.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import za.co.knox.restservice.config.ConfigProperties;
import za.co.knox.restservice.domain.OtpGateWayRequest;
import za.co.knox.restservice.domain.OtpGateWayResponse;
import za.co.knox.restservice.entity.CompanyEntity;
import za.co.knox.restservice.entity.OtpEntity;
import za.co.knox.restservice.enums.KnoxServiceResponseCodesEnum;
import za.co.knox.restservice.enums.EligibilityReasonEnum;
import za.co.knox.restservice.exceptions.KnoxEligibilityException;
import za.co.knox.restservice.exceptions.ServiceException;
import za.co.knox.restservice.repository.OtpRepository;
import za.co.knox.restservice.repository.CompanyRepository;
import za.co.knox.restservice.request.OtpRequest;
import za.co.knox.restservice.request.CompanyRegistrationRequest;
import za.co.knox.restservice.response.EligibilityResponse;
import za.co.knox.restservice.response.OtpResponse;
import za.co.knox.restservice.service.EligibilityService;
import za.co.knox.restservice.service.MessagingService;
import za.co.knox.restservice.service.PropertyBag;
import za.co.knox.restservice.service.CompanyService;
import za.co.knox.restservice.utils.AES;
import za.co.knox.restservice.utils.LoggerUtil;
import za.co.knox.restservice.utils.RestUtil;
import za.co.knox.restservice.utils.Utils;


import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


import static za.co.knox.restservice.utils.RestUtil.configureRestTemplate;


@Service
@Slf4j
public class MessagingServiceImpl implements MessagingService {

    private  ConfigProperties applicationConfig;

    private OtpRepository otpRepository;

    private CompanyRepository companyRepository;

    private PropertyBag propertyBag;

    private EligibilityService eligibilityService;

    private CompanyService companyService;

    private ConfigProperties configProperties;

    @Autowired
    public MessagingServiceImpl(ConfigProperties applicationConfig,
                                OtpRepository otpRepository,
                                CompanyRepository companyRepository,
                                EligibilityService eligibilityService,
                                CompanyService companyService,
                                ConfigProperties configProperties) {


        this.applicationConfig = applicationConfig;
        this.otpRepository = otpRepository;
        this.companyRepository = companyRepository;
        this.eligibilityService = eligibilityService;
        this.companyService = companyService;
        this.configProperties = configProperties;

        propertyBag = new PropertyBag(companyRepository, otpRepository,configProperties);



    }

    @Override
    public void sendPasswordReminder(String mobileNumber) throws ServiceException {

        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("Send password reminder for mobile number ", mobileNumber));

        try {
            CompanyEntity companyEntity = companyRepository.getCompanyEntitiesByMobileNumber(mobileNumber);
            log.info(loggerUtil.info("Found company details", companyEntity));

            if(companyEntity != null) {
                String decryptedPassword = AES.decrypt(companyEntity.getPassword(), configProperties.getSecretKey());
                String retrievedMobileNumber = companyEntity.getMobileNumber();
                String url = applicationConfig.getOtpGateWayUrl();

                String smsBody = String.format("Hi %s, Your KNOX Lite password is '%s'. If you did not request your password please email us at support@knoxsoftware.co.za",companyEntity.getCompanyContactName(), decryptedPassword);
                OtpGateWayRequest otpGateWayRequest = new OtpGateWayRequest(retrievedMobileNumber, smsBody);

                ObjectMapper objectMapper = new ObjectMapper();

                ResponseEntity<String> responseEntity = configureRestTemplate().exchange(url, HttpMethod.POST, new HttpEntity<Object>(otpGateWayRequest, RestUtil.getHeaders(applicationConfig.getOtpGateWayId(), applicationConfig.getOtpGateWayToken())), String.class);

                if (RestUtil.isError(responseEntity.getStatusCode())) {

                    try {
                        OtpGateWayResponse error = objectMapper.readValue(responseEntity.getBody(), OtpGateWayResponse.class);
                        log.error(loggerUtil.error("send password reminder to sms gateway failed with ", error.getTitle()));
                        throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_CALL_SMS_PROVIDER);
                    } catch (JsonProcessingException e) {
                        log.error(loggerUtil.error("send password reminder to sms gateway failed with ", e.getMessage()));
                        throw new ServiceException(KnoxServiceResponseCodesEnum.KNOX_UNHANDLED_EXCEPTION);
                    }
                }
            } else {
                log.error(loggerUtil.error("unable to get company details from the db"));
                throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_GET_COMPANY_DETAILS);
            }

            log.info(loggerUtil.end("Send password reminder for mobile number ", mobileNumber));

        } catch (DataAccessException e) {
            log.error(loggerUtil.error("send password reminder to sms gateway failed with ", e.getMessage()));
            throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_SMS_PASSWORD_REMINDER);
        }

    }

    @Override
    public void generateOtp(CompanyRegistrationRequest companyRegistrationRequest) throws ServiceException {

          LoggerUtil loggerUtil = new LoggerUtil();
          log.info(loggerUtil.start("generate Otp request for ", companyRegistrationRequest.getMobileNumber()));

        try {

            int otpCode = Utils.generateRandomNumbers(1000,9000);

            log.info(loggerUtil.info("Otp: ", otpCode));

            sendOtp(companyRegistrationRequest, otpCode);

            log.info(loggerUtil.end("generate Otp request for ", companyRegistrationRequest.getMobileNumber()));


        } catch (ServiceException e) {
            throw new ServiceException(e.getResponseCodesEnum());
        }

    }

    @Override
    public OtpResponse validateOtp(OtpRequest otpRequest) throws ServiceException {

        LoggerUtil loggerUtil = new LoggerUtil();
        OtpResponse otpResponse;

         propertyBag.setOtpRequest(otpRequest);

        log.info(loggerUtil.start("Otp request for ", otpRequest.getCompanyId()));

        EligibilityResponse eligibilityResponse = eligibilityService.evaluateOtpValidityEligibility(propertyBag);

        if (eligibilityResponse.getEligibilityReason().equals(EligibilityReasonEnum.SUCCESSFUL)) {

            try {
                CompanyRegistrationRequest companyRegistrationRequest = new CompanyRegistrationRequest();
                companyRegistrationRequest.setCompanyId(otpRequest.getCompanyId());
                companyService.updateCompany(companyRegistrationRequest);

                otpResponse = new OtpResponse();
                otpResponse.setResponseCode(0);
                otpResponse.setResponseMessage(KnoxServiceResponseCodesEnum.SUCCESSFUL.getMessage());

                log.info(loggerUtil.end("Otp request for ", otpRequest.getCompanyId()));

                return otpResponse;

            } catch (ServiceException e) {

                throw new ServiceException(e.getResponseCodesEnum());
            }

        } else {
            throw new KnoxEligibilityException(eligibilityResponse.getEligibilityReason().getReasonCode(), eligibilityResponse.getEligibilityReason().getResponseStatus());
        }

    }

    @Override
    public void sendEmail(String content, String recipient, String subject) throws ServiceException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", configProperties.getSmtpUser());
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(configProperties.getSmtpUser(), configProperties.getSmtpPassword());
                    }
                });
        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(configProperties.getSmtpFromEmailAddress()));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            // Set Subject
            message.setSubject(subject);

            // Put the content of your message
            message.setText(content);

            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (Exception e) {
            throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_GET_SEND_EMAIL  );
        }
    }



    private void sendOtp(CompanyRegistrationRequest companyRegistrationRequest, int otpCode) throws ServiceException {

        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("send Otp request to sms gateway for ", companyRegistrationRequest.getMobileNumber()));

        String url = applicationConfig.getOtpGateWayUrl();

        OtpGateWayRequest otpGateWayRequest = new OtpGateWayRequest(companyRegistrationRequest.getMobileNumber(), String.valueOf(otpCode));

        ObjectMapper objectMapper = new ObjectMapper();


            ResponseEntity<String> responseEntity = configureRestTemplate().exchange(url, HttpMethod.POST, new HttpEntity<Object>(otpGateWayRequest, RestUtil.getHeaders(applicationConfig.getOtpGateWayId(), applicationConfig.getOtpGateWayToken())), String.class);

            if (RestUtil.isError(responseEntity.getStatusCode())) {

                try {
                    OtpGateWayResponse error = objectMapper.readValue(responseEntity.getBody(), OtpGateWayResponse.class);
                    log.error(loggerUtil.error("send Otp request to sms gateway failed for ", error.getTitle()));
                    throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_CALL_SMS_PROVIDER);
                } catch (JsonProcessingException e) {
                    log.error(loggerUtil.error("send Otp request to sms gateway failed for ", e.getMessage()));
                    throw new ServiceException(KnoxServiceResponseCodesEnum.KNOX_UNHANDLED_EXCEPTION);
                }
            }

            persistOtp(companyRegistrationRequest, otpCode);
            log.info(loggerUtil.end("send Otp request to sms gateway for ", companyRegistrationRequest.getMobileNumber()));


    }

    private void persistOtp(CompanyRegistrationRequest companyRegistrationRequest, int otpCode) throws ServiceException {

        LoggerUtil loggerUtil = new LoggerUtil();
        log.info(loggerUtil.start("persist Otp to databases ", otpCode));

        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            OtpEntity otpEntity = new OtpEntity();
            otpEntity.setCompanyId(companyRegistrationRequest.getCompanyId());
            otpEntity.setCode(otpCode);
            otpEntity.setTimeReceived(String.valueOf(sdf.format(date.getTime())));
            otpRepository.save(otpEntity);

            log.info(loggerUtil.end("persist Otp to databases ", otpCode));

        } catch (DataAccessException e) {
            log.error(loggerUtil.error("unable to persist otp to database", e.getMessage()));
            throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_PERSIST_OTP  );
        }

    }


}
