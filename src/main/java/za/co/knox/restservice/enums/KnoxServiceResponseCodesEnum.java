package za.co.knox.restservice.enums;


import java.io.Serializable;

/**
 * This class is used for core payment response code identifier
 *
 */
public enum KnoxServiceResponseCodesEnum implements Serializable {


	INVALID_CREDENTIALS(001, "Invalid credentials"),
	USER_AUTHENTICATION_ERROR(001, "User authentication error"),
	REGISTRATION_ERROR(003, "Unable to register user"),
	UNABLE_TO_PERSIST_OTP(004, "Unable to persist otp"),
	UNABLE_TO_CALL_SMS_PROVIDER(005, "Unable to call sms provider"),
	UNABLE_TO_GET_USER_FROM_DB(006, "Unable to get user from db"),
	UNABLE_TO_PERSIST_SCREENING_QUESTIONS(9, "Unable to persist screening question"),
	UNABLE_TO_GET_SCREENING_DATES(10, "Unable to get screening dates"),
	UNABLE_TO_GET_SCREENING_QUESTIONS(11, "Unable to get screening questions"),
	UNABLE_TO_SAVE_COMPANY_USER(12, "Unable to get save company user"),
	UNABLE_TO_GET_COMPANY_USERS(13, "Unable to get company users"),
	UNABLE_TO_SMS_PASSWORD_REMINDER(14, "Unable to sms password reminder"),
	UNABLE_TO_GET_COMPANY_DETAILS(14, "Unable to get company details"),
	UNABLE_TO_GET_COMPANIES_FOR_DAILY_REPORT(15, "Unable to get companies for daily report"),
	UNABLE_TO_GET_SEND_EMAIL(16, "Unable to send email"),
	KNOX_UNHANDLED_EXCEPTION(999, "Knox unhandled exception"),
    SUCCESSFUL(0, "Success");

	private int errorCode;
	private String message;

	public int getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	 KnoxServiceResponseCodesEnum(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

}