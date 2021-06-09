package za.co.knox.restservice.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * All known errors for which there is a mapping must be thrown with this exception. 
 * The Payment Controller will then convert that to the appropriate response.
 */
@Getter
public class TokenEngineServiceException extends RuntimeException {

	@Getter
	@Setter
	private int errorCode;

	@Getter
	@Setter
	private String errorMessage;


	public TokenEngineServiceException(int errorCode, String errorMessage) {
        super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;

	}

	public TokenEngineServiceException(String errorMessage) {
		this.errorMessage = errorMessage;
	}


}