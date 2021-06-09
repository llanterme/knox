package za.co.knox.restservice.exceptions;

import lombok.Getter;
import lombok.Setter;
import za.co.knox.restservice.enums.KnoxServiceResponseCodesEnum;

/**
 * All known errors for which there is a mapping must be thrown with this exception. 
 * The Payment Controller will then convert that to the appropriate response.
 */
@Getter
public class ServiceException extends Exception {

	@Getter
	@Setter
	private KnoxServiceResponseCodesEnum responseCodesEnum;

	public ServiceException(KnoxServiceResponseCodesEnum responseCodesEnum) {
        this.responseCodesEnum = responseCodesEnum;

	}




}