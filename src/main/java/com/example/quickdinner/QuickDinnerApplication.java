package com.example.quickdinner;

import com.example.quickdinner.utils.OberserveurWS;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class QuickDinnerApplication {
	public static OberserveurWS commandeQueuObserver = null;
	public static OberserveurWS playerCommandeObserver = null;
	public static final String ENV = "PROD";

	public static String getHost() {
		String host = "";
		if("DEV".equals(ENV)) {
			host = "http://localhost:86";
		} else if("PROD".equals(ENV)) {
			host = "https://quick-diner.k-gouzien.fr";
		}
		return host;
	}

	public static void main(String[] args) {
		SpringApplication.run(QuickDinnerApplication.class, args);
	}
}
