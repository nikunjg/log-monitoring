package com.htmedia.log.monitor.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Class URLShortenerApplication.
 */
@SpringBootApplication(scanBasePackages = "com.htmedia")
public class LogMonitoringApplication {

	public static void main(final String[] args) {
		SpringApplication.run(LogMonitoringApplication.class, args);
	}
}
