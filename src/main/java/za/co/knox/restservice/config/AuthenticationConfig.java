package za.co.knox.restservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties("app.auth")
public class AuthenticationConfig {

	private AuthenticationUser user;

	private List<AuthenticationUser> users;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AuthenticationUser {
		private String userName;
		private String authorizationHash;
	}

}
