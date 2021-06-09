package za.co.knox.restservice.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("application.yml")
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigProperties {

    @Value("{app.mobileRechargeApiUserName")
    private String mobileRechargeApiUserName;

    @Value("{app.mobileRechargeApiPassword")
    private String mobileRechargeApiPassword;

    @Value("${app.otpGateWayUrl}")
    private String otpGateWayUrl;

    @Value("${app.otpGateWayId}")
    private String otpGateWayId;

    @Value("${app.otpGateWaySecret}")
    private String otpGateWayToken;

    @Value("${app.otpValidity}")
    private String otpValidity;

    @Value("${app.topupDebug}")
    private String topupDebug;

    @Value("${app.secretKey}")
    private String secretKey;

    @Value("${app.smtpUser}")
    private String smtpUser;

    @Value("${app.smtpPassword}")
    private String smtpPassword;

    @Value("${app.smtpServer}")
    private String smtpServer;

    @Value("${app.smtpEnabled}")
    private String smtpEnabled;

    @Value("${app.smtpFromEmailAddress}")
    private String smtpFromEmailAddress;


}