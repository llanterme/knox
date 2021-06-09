package za.co.knox.restservice.provider;


import za.co.knox.restservice.exceptions.ApiAuthenticationException;

public interface ApiAuthenticationProvider {
	
	void authenticateApi(String authorization) throws ApiAuthenticationException;

}
