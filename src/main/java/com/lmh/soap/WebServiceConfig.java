package com.lmh.soap;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import com.lmh.soap.security.Auth;
import com.lmh.soap.security.AuthService;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebServiceConfig.class);

	@Autowired
	private AuthService authService;

	@Bean
	public SimplePasswordValidationCallbackHandler securityCallbackHandler() {
		SimplePasswordValidationCallbackHandler callbackHandler = new SimplePasswordValidationCallbackHandler();
		Properties users = new Properties();

		List<Auth> allRecords = authService.getAllArticles();

		for (Auth ar : allRecords) {
			users.setProperty(ar.getUser_name(), ar.getSecret());
			callbackHandler.setUsers(users);
		}
		/*
		 * Authentication currentUser =
		 * SecurityContextHolder.getContext().getAuthentication(); String username =
		 * currentUser.getName();
		 */
		return callbackHandler;
	}

	@Bean
	public Wss4jSecurityInterceptor securityInterceptor() {
		Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
		securityInterceptor.setValidationActions("Timestamp UsernameToken");
		securityInterceptor.setValidationCallbackHandler(securityCallbackHandler());
		return securityInterceptor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInterceptors(@SuppressWarnings("rawtypes") List interceptors) {
		interceptors.add(securityInterceptor());
	}

	@Bean
	public ServletRegistrationBean<MessageDispatcherServlet> bean(ApplicationContext context) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(context);
		servlet.setTransformSchemaLocations(true);
		return new ServletRegistrationBean<MessageDispatcherServlet>(servlet, "/soap/new-edms/*");
	}

	@Bean(name = { "billslist" })
	public DefaultWsdl11Definition wsdl11Definition(XsdSchema billsListSchema) {
		LOGGER.info("Bills List Services Accessed");
		DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
		definition.setPortTypeName("BillsListPort");
		definition.setLocationUri("https://localhost:8083/soap/new-edms/bills-list");
		definition.setTargetNamespace("http://lmh.com/search-ws");
		definition.setSchema(billsListSchema);
		return definition;
	}

	@Bean
	public XsdSchema billsListSchema() {
		return new SimpleXsdSchema(new ClassPathResource("bills-list.xsd"));
	}

	@Bean(name = { "billobject" })
	public DefaultWsdl11Definition wsdl11Definition1(XsdSchema billObjectSchema) {
		LOGGER.info("Bill Object Services Accessed");
		DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
		definition.setPortTypeName("BillObjectPort");
		definition.setLocationUri("https://localhost:8083/soap/new-edms/billobject");
		definition.setTargetNamespace("http://lmh.com/bill-ws");
		definition.setSchema(billObjectSchema);
		return definition;
	}

	@Bean
	public XsdSchema billObjectSchema() {
		return new SimpleXsdSchema(new ClassPathResource("bill-object.xsd"));
	}
}
