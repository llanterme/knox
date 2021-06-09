package za.co.knox.restservice.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import za.co.knox.restservice.config.ConfigProperties;
import za.co.knox.restservice.domain.CompanyStaff;
import za.co.knox.restservice.entity.CompanyEntity;
import za.co.knox.restservice.entity.CompanyStaffEntity;
import za.co.knox.restservice.enums.EligibilityReasonEnum;
import za.co.knox.restservice.enums.KnoxServiceResponseCodesEnum;
import za.co.knox.restservice.exceptions.KnoxEligibilityException;
import za.co.knox.restservice.exceptions.ServiceException;
import za.co.knox.restservice.repository.CompanyRepository;
import za.co.knox.restservice.repository.CompanyStaffRepository;
import za.co.knox.restservice.repository.OtpRepository;
import za.co.knox.restservice.request.CompanyRegistrationRequest;
import za.co.knox.restservice.response.CompanyRegistrationResponse;
import za.co.knox.restservice.response.EligibilityResponse;
import za.co.knox.restservice.service.CompanyService;
import za.co.knox.restservice.service.EligibilityService;
import za.co.knox.restservice.service.MessagingService;
import za.co.knox.restservice.service.PropertyBag;
import za.co.knox.restservice.utils.AES;
import za.co.knox.restservice.utils.DozerHelper;
import za.co.knox.restservice.utils.LoggerUtil;

import java.util.List;

import static za.co.knox.restservice.utils.DozerHelper.toList;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;

    private CompanyStaffRepository companyStaffRepository;

    private OtpRepository otpRepository;

    private PropertyBag propertyBag;

    private EligibilityService eligibilityService;

    private MessagingService messagingService;

    private ConfigProperties configProperties;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, EligibilityService eligibilityService, OtpRepository otpRepository, @Lazy MessagingService messagingService, CompanyStaffRepository companyStaffRepository
    , ConfigProperties configProperties) {
        this.companyRepository = companyRepository;
        this.eligibilityService = eligibilityService;
        this.otpRepository = otpRepository;
        this.messagingService = messagingService;
        this.companyStaffRepository = companyStaffRepository;
        this.configProperties = configProperties;

        propertyBag = new PropertyBag(companyRepository,otpRepository,configProperties);
    }

    @Override
    public CompanyRegistrationResponse authenticateCompany(CompanyRegistrationRequest companyRegistrationRequest) throws ServiceException {

        propertyBag.setCompanyRegistrationRequest(companyRegistrationRequest);
        LoggerUtil loggerUtil = new LoggerUtil();

        log.info(loggerUtil.start("authenticate company request for ", companyRegistrationRequest.getMobileNumber()));

            EligibilityResponse eligibilityResponse = eligibilityService.evaluateCompanyAuthEligibility(propertyBag);

            if (eligibilityResponse.getEligibilityReason().equals(EligibilityReasonEnum.SUCCESSFUL)) {
               log.info(loggerUtil.end("authenticate user request for ", companyRegistrationRequest.getMobileNumber()));
                return propertyBag.getCompanyRegistrationResponse();
            }  else {
                throw new KnoxEligibilityException(eligibilityResponse.getEligibilityReason().getReasonCode(), eligibilityResponse.getEligibilityReason().getResponseStatus());
            }

    }

    @Override
    public CompanyRegistrationResponse registerCompany(CompanyRegistrationRequest companyRegistrationRequest) throws KnoxEligibilityException, ServiceException {

        LoggerUtil loggerUtil = new LoggerUtil();
        CompanyRegistrationResponse companyRegistrationResponse;
        CompanyEntity createdCompany;

        propertyBag.setCompanyRegistrationRequest(companyRegistrationRequest);

        log.info(loggerUtil.start("register company request for ", companyRegistrationRequest.getMobileNumber()));

        EligibilityResponse eligibilityResponse = eligibilityService.evaluateCompanyRegistrationEligibility(propertyBag);

        if (eligibilityResponse.getEligibilityReason().equals(EligibilityReasonEnum.SUCCESSFUL)) {

            try {
                createdCompany = persistCompany(companyRegistrationRequest, true);
                companyRegistrationRequest.setCompanyId(createdCompany.getCompanyId());
                companyRegistrationRequest.setCompanyIdentifier(createdCompany.getCompanyIdentifier());
                companyRegistrationResponse = new DozerBeanMapper().map(createdCompany, CompanyRegistrationResponse.class);

                return companyRegistrationResponse;
            } catch (ServiceException e) {
                throw new ServiceException(e.getResponseCodesEnum());
            }

        } else {
            throw new KnoxEligibilityException(eligibilityResponse.getEligibilityReason().getReasonCode(), eligibilityResponse.getEligibilityReason().getResponseStatus());
        }

    }

    @Override
    public CompanyRegistrationResponse updateCompany(CompanyRegistrationRequest companyRegistrationRequest) throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();
        CompanyEntity userToUpdate;
        try {
            userToUpdate = companyRepository.findById(companyRegistrationRequest.getCompanyId()).get();

        } catch (DataAccessException e) {
            log.error(loggerUtil.error("unable to get user from db ", e.getMessage()));
            throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_GET_USER_FROM_DB);
        }

        try {
            persistCompany(companyRegistrationRequest,false);
            log.info(loggerUtil.end("update user request for ", CompanyRegistrationRequest.class));
            return new DozerBeanMapper().map(userToUpdate, CompanyRegistrationResponse.class);

        } catch (ServiceException e) {
            log.error(loggerUtil.error("unable to update user ", e.getMessage()));
            throw new ServiceException(e.getResponseCodesEnum());
        }
    }

    private CompanyEntity persistCompany(CompanyRegistrationRequest companyRegistrationRequest, boolean isNewCompany) throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();

        try {
            Long numberValidated;
            int companyId;
            if(isNewCompany){
                numberValidated = new Long(0);
                companyId = 0;
            } else {
                numberValidated = new Long(1);
                companyId = companyRegistrationRequest.getCompanyId();
            }

            CompanyEntity companyEntity = new CompanyEntity();
            companyEntity.setEmailAddress(companyRegistrationRequest.getEmailAddress());
            companyEntity.setMobileNumber(companyRegistrationRequest.getMobileNumber());
            companyEntity.setPassword(AES.encrypt(companyRegistrationRequest.getPassword(), configProperties.getSecretKey()));
            companyEntity.setCompanyName(companyRegistrationRequest.getCompanyName());
            companyEntity.setNumberValidated(numberValidated);
            companyEntity.setCompanyAddress(companyRegistrationRequest.getCompanyAddress());
            companyEntity.setCompanyId(companyId);
            companyEntity.setCompanyIdentifier(java.util.UUID.randomUUID().toString());
            companyEntity.setCompanyContactName(companyRegistrationRequest.getCompanyContactName());
            companyEntity.setAccountLocked("0");
            companyRepository.save(companyEntity);


            return companyEntity;
        } catch (DataAccessException e) {

            //TODO Make this more generic for updating or creating
            throw new ServiceException(KnoxServiceResponseCodesEnum.REGISTRATION_ERROR);
        }

    }

    @Override
    public void addCompanyUser(CompanyStaff companyStaff) throws ServiceException {
        LoggerUtil loggerUtil = new LoggerUtil();
     try {
            log.info(loggerUtil.start("create company staff member request for ", companyStaff));
            if(!companyStaffRepository.validateStaffUserAlreadyRegistered(companyStaff.getUserMobile())) {
                companyStaffRepository.save(new DozerBeanMapper().map(companyStaff, CompanyStaffEntity.class));
            }
            log.info(loggerUtil.end("create company staff member request for ", companyStaff));

        } catch (DataAccessException e) {
            log.error(loggerUtil.error("unable to save company staff member ", e.getMessage()));
            throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_SAVE_COMPANY_USER);
        }

    }

    @Override
    public List<CompanyStaff> getCompanyStaff(int companyId) throws ServiceException {

        LoggerUtil loggerUtil = new LoggerUtil();
        List<CompanyStaff> companyStaffList;
        try {
            log.info(loggerUtil.start("get company staff list request for ", companyId));

            List<CompanyStaffEntity> companyStaffEntities = companyStaffRepository.getCompanyStaffEntityByCompanyId(companyId);

            companyStaffList = DozerHelper.map(new DozerBeanMapper(), toList(companyStaffEntities), CompanyStaff.class);

            log.info(loggerUtil.end("get company staff list request for ", companyId));

            return companyStaffList;

        }catch (DataAccessException e) {
            log.error(loggerUtil.end("unable to get company users list for ", companyId));
            throw new ServiceException(KnoxServiceResponseCodesEnum.UNABLE_TO_GET_COMPANY_USERS);
        }
    }
}
