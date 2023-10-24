///*
// *  RequestInterceptor.java
// *
// *  Copyright © 2023 ING Group. All rights reserved.
// *
// *  This software is the confidential and proprietary information of
// *  ING Group ("Confidential Information").
// */
//package com.jfcm.manda.bookingmanagerapi.utils;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Random;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//@Component
//public class RequestInterceptor extends HandlerInterceptorAdapter implements RequestDataInterceptor {
//
//  @Override
//  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//    String parameter = request.getParameter("id");
//    if (StringUtils.isEmpty(parameter)) {
//      request.setAttribute("customAttribute", "JF" + getRandomNumbers());
//    }
//
//    return true;
//  }
//
//  public String getRandomNumbers() {
//    Random random = new Random();
//    int randomNum = random.nextInt(99999);
//
//    return Integer.toString(randomNum);
//  }
//}