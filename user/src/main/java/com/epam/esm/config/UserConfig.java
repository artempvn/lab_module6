package com.epam.esm.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Configuration
public class UserConfig {

  public static final int CONNECTION_POOL_SIZE = 10;
  @Value("${keycloak.auth-server-url}")
  private String serverUrl;

  @Value("${kc.realm}")
  private String realm;

  @Value("${kc.client}")
  private String clientId;

  @Value("${kc.user}")
  private String userName;

  @Value("${kc.password}")
  private String password;

  @Bean
  public LocalValidatorFactoryBean getValidator() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource());
    return bean;
  }

  @Bean
  public LocaleResolver localeResolver() {
    AcceptHeaderLocaleResolver sessionLocaleResolver = new AcceptHeaderLocaleResolver();
    sessionLocaleResolver.setDefaultLocale(Locale.US);
    return sessionLocaleResolver;
  }

  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setBasename("classpath:message");
    return messageSource;
  }

  @Bean
  public Keycloak keycloak() {
    return KeycloakBuilder.builder()
        .serverUrl(serverUrl)
        .realm(realm)
        .grantType(OAuth2Constants.PASSWORD)
        .username(userName)
        .password(password)
        .clientId(clientId)
        .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(CONNECTION_POOL_SIZE).build())
        .build();
  }
}
