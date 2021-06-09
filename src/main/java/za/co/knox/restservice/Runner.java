package za.co.knox.restservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import za.co.knox.restservice.utils.AES;


public class Runner {

    public static void main(String[] args) {

        String key = "Y1cR4$P8[6,Nr61";

        String encryptedPassword = AES.encrypt("Passw0rd1", key);
        System.out.println(encryptedPassword);

    }




}
