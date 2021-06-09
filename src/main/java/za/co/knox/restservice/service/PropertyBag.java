package za.co.knox.restservice.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.knox.restservice.config.ConfigProperties;
import za.co.knox.restservice.repository.OtpRepository;
import za.co.knox.restservice.repository.CompanyRepository;
import za.co.knox.restservice.request.OtpRequest;
import za.co.knox.restservice.request.CompanyRegistrationRequest;
import za.co.knox.restservice.response.CompanyRegistrationResponse;

@Slf4j
public class PropertyBag {


    @Getter
    @Setter
    private CompanyRegistrationRequest companyRegistrationRequest;

    @Getter
    @Setter
    private OtpRequest otpRequest;

    @Getter
    @Setter
    private CompanyRegistrationResponse companyRegistrationResponse;

    @Getter
    @Setter
    private CompanyRepository companyRepository;

    @Getter
    @Setter
    private OtpRepository otpRepository;

    @Getter
    private ConfigProperties configProperties;


    @Autowired
    public PropertyBag(CompanyRepository companyRepository, OtpRepository otpRepository, ConfigProperties configProperties) {

        this.companyRepository = companyRepository;
        this.otpRepository = otpRepository;
        this.configProperties = configProperties;


    }





}