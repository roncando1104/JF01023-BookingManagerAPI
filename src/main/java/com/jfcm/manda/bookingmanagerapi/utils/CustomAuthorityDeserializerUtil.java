/*
 *  CustomAuthorityDeserializerUtil.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CustomAuthorityDeserializerUtil extends JsonDeserializer {

  @Override
  public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    ObjectCodec mapper = jsonParser.getCodec();
    JsonNode jsonNode = mapper.readTree(jsonParser);
    List<GrantedAuthority> grantedAuthorities = new LinkedList<>();

    Iterator<JsonNode> elements = jsonNode.elements();
    while (elements.hasNext()) {
      JsonNode next = elements.next();
      JsonNode authority = next.get("authority");
      grantedAuthorities.add(new SimpleGrantedAuthority(authority.asText()));
    }
    return grantedAuthorities;
  }
}