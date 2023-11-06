/**
 * {@link com.jfcm.manda.bookingmanagerapi.config.OpenAPIConfig}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "Booking Manager API",
        version = "1.0.0",
        description = "Booking Data Rest for Room Reservation",
        termsOfService = "Terms of Services...",
        contact = @Contact(
            name = "JFCM Mandaluyong",
            url = "http://jfcm-manda/dummy/url",
            email = "ron.cando04@gmail.com"
        ),
        license = @License(
            name = "Booking Manager API 1.0.0",
            url = "http://jfcm-manda/dummy/url"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080/",
            description = "LOCAL ENV")
    }
)
public class OpenAPIConfig {

}