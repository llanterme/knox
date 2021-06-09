package za.co.knox.restservice.provider.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import za.co.knox.restservice.config.AuthenticationConfig;
import za.co.knox.restservice.exceptions.ApiAuthenticationException;
import za.co.knox.restservice.provider.ApiAuthenticationProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@EnableConfigurationProperties
@Component
public class ApiAuthenticationProviderImpl implements ApiAuthenticationProvider {
	
	private AuthenticationConfig authConfig;
	
	@Autowired
	public ApiAuthenticationProviderImpl(AuthenticationConfig authConfig) {
		this.authConfig = authConfig;
	}
	
	@Override
	public void authenticateApi(String authorization) throws ApiAuthenticationException {

		String hashedPassword = generateHashPassword(authorization);
		Boolean isAuthenticated = false;
		List<AuthenticationConfig.AuthenticationUser> paymentServiceUsers = authConfig.getUsers();
		if (paymentServiceUsers != null && !paymentServiceUsers.isEmpty()) {
			for (AuthenticationConfig.AuthenticationUser paymentServiceUser : paymentServiceUsers) {
				if (paymentServiceUser.getAuthorizationHash().equals(hashedPassword)) {
					isAuthenticated = true;
					break;
				}
			}
		} 

		if(!isAuthenticated) {
			throw new ApiAuthenticationException("Invalid Credentials.");
		}

	}
	
	private String generateHashPassword(String input) {

		StringBuilder hash = new StringBuilder();
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
			for (int idx = 0; idx < hashedBytes.length; ++idx) {
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			throw new ApiAuthenticationException("Error generating hash password.");
		}
		String hashedPassword = hash.toString();
		return hashedPassword;
}

}
