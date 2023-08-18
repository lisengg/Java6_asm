package com.poly;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
//import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
//import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
//import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
//import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import com.poly.bean.Account;
import com.poly.service.AccountService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	AccountService accountService;
	@Autowired
	BCryptPasswordEncoder pe;
	
	//Cung cấp nguồn dữ liệu đăng nhập
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(username -> {
			try {
				Account user = accountService.findById(username);
				String password = pe.encode(user.getPassword());
				String[] roles = user.getAuthorities().stream()
					.map(er -> er.getRole().getId())
					.collect(Collectors.toList()).toArray(new String[0]);
				return User.withUsername(username).password(password).roles(roles).build();
			} catch (NoSuchElementException e) {
				throw new UsernameNotFoundException(username + "not found!");
			}
		});
	}
	
	/*--Phân quyền sử dụng và hình thức đăng nhập--*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// CSRF, CORS
		http.csrf().disable();	
		// Phân quyền sử dụng
		http.authorizeRequests()
			.antMatchers("/order/**").authenticated()
			.antMatchers("/admin/**").hasAnyRole("STAF", "DIRE")
			.antMatchers("/rest/authorities").hasRole("DIRE")
			.anyRequest().permitAll(); // anonymous
		// Giao diện đăng nhập
			http.formLogin()
				.loginPage("/security/login/form")
				.loginProcessingUrl("/security/login") // [/login]
				.defaultSuccessUrl("/security/login/success", false)
				.failureUrl("/security/login/error");
			
			//Oauth2Login
				http.oauth2Login()
				.loginPage("/oauth2/login/form")
				.defaultSuccessUrl("/oauth2/login/success", true)
				.failureUrl("/oauth2/login/error")
				.authorizationEndpoint().baseUri("/oauth2/authorization")
				.authorizationRequestRepository(getRepository())
				.and().tokenEndpoint()
				.accessTokenResponseClient(getToken());
				
			http.rememberMe()
				.tokenValiditySeconds(86400);
			

			
		// Điều khiển lỗi truy cập không đúng vai trò
		http.exceptionHandling()
			.accessDeniedPage("/security/unauthoritied"); // [/error]
		// Đăng xuất
		http.logout()
			.logoutUrl("/security/logoff") // [/logout]
			.logoutSuccessUrl("/security/logoff/success");	
	}
	
	@Bean
	public AuthorizationRequestRepository<OAuth2AuthorizationRequest> getRepository(){
		return new HttpSessionOAuth2AuthorizationRequestRepository();
	}
	
	@Bean
	public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> getToken(){
		return new DefaultAuthorizationCodeTokenResponseClient();
	}
	
	//cơ chế mã hóa mật khẩu
	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//Cho phép truy xuất RESR API từ bên ngoài(domain khác)
	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
	}
}