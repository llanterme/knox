package za.co.knox.restservice.service;


import za.co.knox.restservice.exceptions.TokenEngineServiceException;
import za.co.knox.restservice.response.EligibilityResponse;

public interface EligibilityService {

     EligibilityResponse evaluateCompanyRegistrationEligibility(final PropertyBag propertyBag) throws TokenEngineServiceException;

     EligibilityResponse evaluateCompanyAuthEligibility(final PropertyBag propertyBag) throws TokenEngineServiceException;

     EligibilityResponse evaluateOtpValidityEligibility(final PropertyBag propertyBag) throws TokenEngineServiceException;


}
