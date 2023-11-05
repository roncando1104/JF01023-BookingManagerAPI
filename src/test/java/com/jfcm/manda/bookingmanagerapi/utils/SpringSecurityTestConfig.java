///*
// *  SpringSecurityTestConfig.java
// *
// *  Copyright © 2023 ING Group. All rights reserved.
// *
// *  This software is the confidential and proprietary information of
// *  ING Group ("Confidential Information").
// */
//package com.jfcm.manda.bookingmanagerapi.utils;
//
//import org.osgi.service.useradmin.User;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//@TestConfiguration
//public class SpringSecurityTestConfig {
//
//  @Bean
//  @Primary
//  public UserDetailsService userDetailsService() {
//    User basicUser = new UserImpl("Basic User", "user@company.com", "password");
//    UserActive basicActiveUser = new UserActive(basicUser, Arrays.asList(
//        new SimpleGrantedAuthority("ROLE_USER"),
//        new SimpleGrantedAuthority("PERM_FOO_READ")
//    ));
//
//    User managerUser = new UserImpl("Manager User", "manager@company.com", "password");
//    UserActive managerActiveUser = new UserActive(managerUser, Arrays.asList(
//        new SimpleGrantedAuthority("ROLE_MANAGER"),
//        new SimpleGrantedAuthority("PERM_FOO_READ"),
//        new SimpleGrantedAuthority("PERM_FOO_WRITE"),
//        new SimpleGrantedAuthority("PERM_FOO_MANAGE")
//    ));
//
//    return new InMemoryUserDetailsManager(Arrays.asList(
//        basicActiveUser, managerActiveUser
//    ));
//  }
//}