package org.onap.sdnc.apps.ms.gra;

import jakarta.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class UriMatcherObjectMapperResolver implements ObjectMapperResolver {
	private static final Logger log = LoggerFactory.getLogger(UriMatcherObjectMapperResolver.class);

    private final ObjectMapper defaultMapper;
    private final ObjectMapper wrappedMapper;
    private final ObjectMapper unwrappedMapper;
  
    public UriMatcherObjectMapperResolver(ObjectMapper defaultMapper) {

      defaultMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      defaultMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      this.defaultMapper = defaultMapper;
      this.wrappedMapper = defaultMapper.copy();
      wrappedMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
      wrappedMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
      this.unwrappedMapper = defaultMapper.copy();
      unwrappedMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
      unwrappedMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
    }
  
    @Override
    public ObjectMapper getObjectMapper() {
      ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

      if (sra == null) {
        log.debug("Using default objectmapper based on null ServletRequestAttributes");
        return(defaultMapper);
      }
      HttpServletRequest request = sra.getRequest();

      String uri = request.getRequestURI();
      if (uri.startsWith("/restconf/")) {
        uri = uri.replaceFirst("/restconf/", "/");
      }

      if (uri.startsWith("/config/") || uri.startsWith("/operational/")) {
        log.debug("Using wrapped objectmapper based on uri {}", uri);
        return(wrappedMapper);
      } else if (uri.startsWith("/operations/")) {
        log.debug("Using unwrapper objectmapper based on uri {}", uri);
        return(unwrappedMapper);
      }
      
      log.debug("Using default objectmapper based on uri {}", uri);
      return defaultMapper;
    }
  
  }
