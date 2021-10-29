package org.onap.sdnc.apps.ms.gra;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GraWebConfiguration implements WebMvcConfigurer {

    @Autowired
    ObjectMapper objectMapper;

    @Value("${swagger-ui.host:localhost}")
    private String swaggerUiHost;

    private ObjectMapper objectMapperProxy() {
        ObjectMapperResolver objectMapperResolver = new UriMatcherObjectMapperResolver(objectMapper);
        ProxyFactory factory = new ProxyFactory();
        factory.setTargetClass(ObjectMapper.class);
        factory.addAdvice(new ObjectMapperInterceptor() {

            @Override
            protected ObjectMapper getObject() {
                return objectMapperResolver.getObjectMapper();
            }

        });

        return (ObjectMapper) factory.getProxy();
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(objectMapperProxy());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jackson2HttpMessageConverter());

    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("https://" + swaggerUiHost);
            }
        };
    }
}
