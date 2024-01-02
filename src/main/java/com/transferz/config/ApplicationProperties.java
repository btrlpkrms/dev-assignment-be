package com.transferz.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the application.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "config")
public class ApplicationProperties {

    /**
     * The maximum number of passengers allowed on a flight.
     * This value is configurable through application properties.
     */
    private int maxPassengerCount;


}
