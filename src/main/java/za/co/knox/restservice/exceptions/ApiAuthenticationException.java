package za.co.knox.restservice.exceptions;

import lombok.Getter;

/**
 * All known errors for which there is a mapping must be thrown with this exception. 
 * The Payment Controller will then convert that to the appropriate response.
 */
@Getter
public class ApiAuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
    private String errorMessage;

	public ApiAuthenticationException(String errorMessage) {
        this.errorMessage = errorMessage;
	}

}