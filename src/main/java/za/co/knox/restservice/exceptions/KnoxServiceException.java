package za.co.knox.restservice.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * All known errors for which there is a mapping must be thrown with this exception. 
 * The Payment Controller will then convert that to the appropriate response.
 */
@Getter
public class KnoxServiceException extends RuntimeException {

	@Getter
	@Setter
	private String errorCode;

	@Getter
	@Setter
	private String errorMessage;


	public KnoxServiceException(String errorCode, String errorMessage) {
        super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;

	}

	public KnoxServiceException(String errorMessage) {
		this.errorMessage = errorMessage;
	}


}