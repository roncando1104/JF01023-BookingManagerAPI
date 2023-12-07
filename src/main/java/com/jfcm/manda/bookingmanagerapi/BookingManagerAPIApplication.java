/**
 * {@link com.jfcm.manda.bookingmanagerapi.BookingManagerAPIApplication}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi;

import java.util.Arrays;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class BookingManagerAPIApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookingManagerAPIApplication.class, args);
  }

//  @Bean
//  public CorsWebFilter corsWebFilter() {
//    CorsConfiguration corsConfig = new CorsConfiguration();
//    corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));
//    corsConfig.setMaxAge(3600L);
//    corsConfig.addAllowedMethod("*");
//    corsConfig.addAllowedHeader("Requestor-Type");
//    corsConfig.addExposedHeader("X-Get-Header");
//
//    UrlBasedCorsConfigurationSource source =
//        new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", corsConfig);
//
//    return new CorsWebFilter(source);
//  }

//  @Bean
//  public WebMvcConfigurer corsConfigurer() {
//    return new WebMvcConfigurer() {
//      @Override
//      public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/users").allowedOrigins("http://localhost:3000");
//      }
//    };
//  }

//  private static String generateAllDatesFromGivenYear() {
//    List<String> a = Arrays.stream(Month.values()).map(Enum::name).toList();
//
//    String alldates = null;
//
//    for (String month : a) {
//      YearMonth yearMonth = YearMonth.of(2013, Month.valueOf(month));
//      LocalDate firstOfMonth = yearMonth.atDay(1);
//      LocalDate firstOfFollowingMonth = yearMonth.plusMonths(1).atDay(1);
//      //firstOfMonth.datesUntil(firstOfFollowingMonth).forEach(System.out::println);
//      List<String> s = firstOfMonth.datesUntil(firstOfFollowingMonth).map(LocalDate::toString).toList();
//      //String s = firstOfMonth.datesUntil(firstOfFollowingMonth).map(LocalDate::toString).collect(Collectors.joining(" "));
//      //Long sInt = firstOfMonth.datesUntil(firstOfFollowingMonth).count();
//
//      JSONArray jsonArray = new JSONArray();
//
//      for (String string : s) {
//        JSONObject object = new JSONObject();
//        object.put("dates", string);
//        alldates = String.valueOf(object);
//        System.out.println(object);
//
//        return alldates;
//        //System.out.println(object);
//      }
//
//    }
//   return null;
//  }

  //System.out.println(generateAllDatesFromGivenYear());
  //generateAllDatesFromGivenYear();

//    List<String> a = Arrays.stream(Month.values()).map(Enum::name).toList();
//
//    String alldates;
//    ObjectMapper mapper = new ObjectMapper();
//    for (String month : a) {
//      YearMonth yearMonth = YearMonth.of(YearMonth.now().getYear(), Month.valueOf(month));
//      LocalDate firstOfMonth = yearMonth.atDay(1);
//      LocalDate firstOfFollowingMonth = yearMonth.plusMonths(1).atDay(1);
//      //firstOfMonth.datesUntil(firstOfFollowingMonth).forEach(System.out::println);
//      List<String> s = firstOfMonth.datesUntil(firstOfFollowingMonth).map(LocalDate::toString).toList();
//      //String s = firstOfMonth.datesUntil(firstOfFollowingMonth).map(LocalDate::toString).collect(Collectors.joining(" "));
//      //Long sInt = firstOfMonth.datesUntil(firstOfFollowingMonth).count();
//
//      JSONArray jsonArray = new JSONArray();
//
//      for (String string : s) {
//        JSONObject object = new JSONObject();
//        object.put("id", 1);
//        object.put("dates", string);
//        alldates = String.valueOf(object);
//        System.out.println(new JSONObject(alldates));
//
//        //return alldates;
//        //System.out.println(object);
//      }
//
//    }
//    //return null;

}
