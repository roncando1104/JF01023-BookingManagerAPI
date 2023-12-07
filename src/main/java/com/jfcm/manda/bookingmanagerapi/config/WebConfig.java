/*
 *  WebConfig.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.config;

import java.util.Arrays;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class WebConfig {

  private static final Long MAX_AGE = 3600L;
  private static final int CORS_FILTER_ORDER = -102;

  @Bean
  public FilterRegistrationBean<CorsFilter> corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = getCorsConfiguration();
    source.registerCorsConfiguration("/**", config);
    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));

    // should be set order to -100 because we need to CorsFilter before SpringSecurityFilter
    bean.setOrder(CORS_FILTER_ORDER);
    return bean;
  }

  private static CorsConfiguration getCorsConfiguration() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("http://localhost:3000");//put in application.yml file
    config.setAllowedHeaders(Arrays.asList(
        HttpHeaders.AUTHORIZATION,
        HttpHeaders.CONTENT_TYPE,
        HttpHeaders.ACCEPT));
    config.setAllowedMethods(Arrays.asList(
        HttpMethod.OPTIONS.name(),
        HttpMethod.GET.name(),
        HttpMethod.POST.name(),
        HttpMethod.PUT.name(),
        HttpMethod.DELETE.name()));
    config.setMaxAge(MAX_AGE);
    return config;
  }
}