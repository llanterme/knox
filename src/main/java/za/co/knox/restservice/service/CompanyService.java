package za.co.knox.restservice.service;

import za.co.knox.restservice.domain.CompanyStaff;
import za.co.knox.restservice.exceptions.KnoxEligibilityException;
import za.co.knox.restservice.exceptions.ServiceException;
import za.co.knox.restservice.request.CompanyRegistrationRequest;
import za.co.knox.restservice.response.CompanyRegistrationResponse;

import java.util.List;


public interface CompanyService {

    CompanyRegistrationResponse authenticateCompany(CompanyRegistrationRequest companyRegistrationRequest) throws ServiceException;

    CompanyRegistrationResponse registerCompany(CompanyRegistrationRequest companyRegistrationRequest) throws KnoxEligibilityException, ServiceException;

    CompanyRegistrationResponse updateCompany(CompanyRegistrationRequest companyRegistrationRequest) throws ServiceException;

    void addCompanyUser(CompanyStaff companyStaff) throws ServiceException;

    List<CompanyStaff> getCompanyStaff(int companyId) throws ServiceException;


}
