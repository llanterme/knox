package za.co.knox.restservice.enums;


import java.io.Serializable;

/**
 * This class is used for core payment response code identifier
 *
 */
public enum KnoxResponseCodesEnum implements Serializable {


	AUTHENTICATION_ERROR(1001, "Invalid credentials"),
	API_AUTHENTICATION_ERROR(1001, "Api authentication error"),
	ELIGIBILITY_ERROR(1002, "Eligibility failed"),
    DIGITAL_COWBOY_SERVICE_EXCEPTION(003, "Digital cowboy service exception"),
    SUCCESSFUL(0, "Success");

	private int errorCode;
	private String message;

	public int getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	 KnoxResponseCodesEnum(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

}