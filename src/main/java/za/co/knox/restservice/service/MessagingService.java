package za.co.knox.restservice.service;


import za.co.knox.restservice.exceptions.ServiceException;
import za.co.knox.restservice.request.OtpRequest;
import za.co.knox.restservice.request.CompanyRegistrationRequest;
import za.co.knox.restservice.response.OtpResponse;

/**
 * Created by Luke on 3/23/18.
 */
public interface MessagingService {

    void generateOtp(CompanyRegistrationRequest companyRegistrationRequest) throws ServiceException;

    OtpResponse validateOtp(OtpRequest otpRequest) throws ServiceException;

    void sendPasswordReminder(String mobileNumber) throws ServiceException;

    void sendEmail(String content, String recipient,String subject) throws ServiceException;

}
