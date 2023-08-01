package com.softpuzzle.angkor.config.swagger;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration // 스프링 실행시 설정파일 읽어드리기 위한 어노테이션
@EnableSwagger2 // Swagger2를 사용하겠다는 어노테이션
@SuppressWarnings("unchecked") // warning밑줄 제거를 위한 태그
public class SwaggerConfig extends WebMvcConfigurationSupport {

	private String version;
	private String title;
	
	@Bean
	public Docket signinV1() {
		version = "AngkorLogin 1.0";
		title = "AngkorChat signin API " + version;

		return new Docket(DocumentationType.SWAGGER_2) //
				.securityContexts(Arrays.asList(securityContext())) //
				.securitySchemes(Arrays.asList(apiKey())) //
				.useDefaultResponseMessages(true) //
				.groupName(version) //
				.select() //
				.apis(RequestHandlerSelectors.basePackage("com.softpuzzle.angkor.controller.signin")) //
				.paths(PathSelectors.any()) //
				.build() //
				.apiInfo(apiInfo(title, version)); //

	}
	

	@Bean
	public Docket apiV1() {
		version = "AngkorChat 1.0";
		title = "AngkorChat API " + version;

		return new Docket(DocumentationType.SWAGGER_2) //
				.securityContexts(Arrays.asList(securityContext())) //
				.securitySchemes(Arrays.asList(apiKey())) //
				.useDefaultResponseMessages(true) //
				.produces(getProduceContentTypes())//
				.groupName(version) //
				.select() //
				.apis(RequestHandlerSelectors.basePackage("com.softpuzzle.angkor.controller.api")) //
				.paths(PathSelectors.any()) //
				.build() //
				.apiInfo(apiInfo(title, version)); //

	}

	@Bean
	public Docket apiV2() {
		version = "SendBirdSDK 1.0";
		title = "SendBirdSDK API " + version;

		return new Docket(DocumentationType.SWAGGER_2) //
				.securityContexts(Arrays.asList(securityContext()))//
				.securitySchemes(Arrays.asList(apiKey()))//
				.useDefaultResponseMessages(true)//
				.groupName(version)//
				.select()//
				.apis(RequestHandlerSelectors.basePackage("com.softpuzzle.angkor.controller.sendbird"))//
				.paths(PathSelectors.any())//
				.build()//
				.apiInfo(apiInfo(title, version));//

	}

	private Set<String> getProduceContentTypes() {
		Set<String> produces = new HashSet<>();
		produces.add("application/json");
		return produces;
	}

	private ApiInfo apiInfo(String title, String version) {
		return new ApiInfoBuilder()//
				.title(title)//
				.description("이 API 경우<strong> [AngkorChat API] </strong>를 제공합니다. <br> " +
							"<a href='http://http://auth.angkorcoms.com/swagger/index.html' target='_blank'>링크 참조</a><br>"+
							"<p></p><u><h3> API 호출 시나리오</h3></u><p></p>"+
							"<p></p><h3>[로그인]</h3><p></p>"+
							"<p></p><h4>A. SMS인증이 필요한 경우</h4><p></p>"+
							"<p></p><h5>1. 사용자 아이디, 비밀번호로 로그인 : [POST] /ankochat/api/login/signin =&gt; 400417 오류 리턴 (등록된 기기와 다름)</h5><p></p>"+
							"<p></p><h5>2. SMS인증 요청 : [POST] /ankochat/api/smscert/req</h5><p></p>"+
							"<p></p><h5>3. SMS인증번호 확인 : [POST] /ankochat/api/smscert/cert =&gt; 로그인 완료</h5><p></p>"+
							"<p></p><h4>B. SMS인증이 필요없는 경우</h4><p></p>"+
							"<p></p><h5>1. 사용자 아이디, 비밀번호로 로그인 : [POST] /ankochat/api/login/signin =&gt; 200 OK =&gt; 로그인 완료</h5><p></p>"+
							"<p></p><h3>[비밀번호 업데이트 : 비밀번호 분실]</h3><p></p>"+
							"<p></p><h5>1. 사용가능 이메일 확인(이미 등록된 이메일인지 확인 : 옵션) : [POST] /ankochat/api/user/email</h5><p></p>"+
							"<p></p><h5>2. 이메일 인증코드 발급(이메일 인증 서버에서 인증코드 요청)</h5><p></p>"+
							"<p></p><h5>- 이메일 인증코드 요청 : [POST] /ankochat/api/emailcert/req</h5><p></p>"+
							"<p></p><h5>3. 이메일 인증코드로 회원 비밀번호 업데이트 : [POST] /ankochat/api/user/pw </h5><p></p>"+
							"<p></p><h3>[전화번호 업데이트]</h3><p></p>"+
							"<p></p><h5>1. SMS인증 요청 : [POST] /ankochat/api/smscert/req</h5><p></p>"+
							"<p></p><h5>2. SMS인증번호 확인 : [POST] /ankochat/api/smscert/cert</h5><p></p>"+
							"<p></p><h5>3. 전화번호 업데이트 : [POST] /ankochat/api/user/phone</h5><p></p>"+
							"<p></p><h3>[회원가입]</h3><p></p>"+
							"<p></p><h5>1. 약관 동의 : [POST] /ankochat/api/agree/agree/{language}</h5><p></p>"+
							"<p></p><h5>2. SMS인증 요청 : [POST] /ankochat/api/smscert/req</h5><p></p>"+
							"<p></p><h5>3. SMS인증번호 확인 : [POST] /ankochat/api/smscert/cert</h5><p></p>"+
							"<p></p><h5>5. 사용가능 아이디 확인(이미 등록된 아이디인지 확인 : 필수) : [POST] /ankochat/api/user/checkUserId</h5><p></p>"+
							"<p></p><h5>6. 사용가능 이메일 확인(이미 등록된 이메일인지 확인 : 옵션) : [POST] /ankochat/api/user/email</h5><p></p>"+
							"<p></p><h5>7. 이메일 인증코드 발급(이메일 인증 서버에서 인증코드 요청 : 옵션)</h5><p></p>"+
							"<p></p><h5>- 이메일 인증코드 요청 : [POST] /ankochat/api/emailcert/req</h5><p></p>"+
							"<p></p><h5>8. 이메일 인증코드 확인 (옵션) : [POST] /ankochat/api/emailcert/cert</h5><p></p>"+
							"<p></p><h5>9. 회원가입 : [POST] /ankochat/api/user/signup</h5><p></p>"
				)
				.version("1.0.0")//
				.build();
	}

	private ApiKey apiKey() {
		return new ApiKey("Authorization", "Authorization", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
	}

	// 리소스 핸들러 설
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/resources/static/swagger/theme-muted.css");
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	// swagger ui 설정.
	@Bean
	public UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder() //
				.displayRequestDuration(true) //
				.validatorUrl("") //
				.build();
	}

	@Bean
	public LinkDiscoverers discoverers() {
		List<LinkDiscoverer> plugins = new ArrayList<>();
		plugins.add(new CollectionJsonLinkDiscoverer());
		return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


}
