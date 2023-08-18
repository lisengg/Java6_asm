package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@CrossOrigin("*")
@Controller
public class SecurityController {
	
	
	@RequestMapping("/security/login/form")
	public String loginForm(Model model) {
		model.addAttribute("message", "Vui lòng đăng nhập!");
		return "security/login";
	}
	
	@RequestMapping("/security/login/success")
	public String loginSuccess(Model model) {
		model.addAttribute("message", "Đăng nhập thành công!");
		return "security/login";
	}
	
	@RequestMapping("/security/login/error")
	public String loginError(Model model) {
		model.addAttribute("message", "Sai thông tin đăng nhập!");
		return "security/login";
	}
	
	@RequestMapping("/security/unauthoritied")
	public String unauthoritied(Model model) {
		model.addAttribute("message", "Không có quyền truy xuất!");
		return "product/list";
	}
	
	@RequestMapping("/security/logoff/success")
	public String logoffSuccess(Model model) {
		model.addAttribute("message", "Bạn đã đăng xuất!");
		return "security/login";
	}
	
	//Oauth2
	@GetMapping("/oauth2/login/form")
	public String form() {
		return "security/login";
	}
	
	@GetMapping("/oauth2/login/success")
	public String success(OAuth2AuthenticationToken oauth,Model model) {
		String email = oauth.getPrincipal().getAttribute("email");
		String name = oauth.getPrincipal().getAttribute("name");
		
		UserDetails user = User.withUsername(email).password("").roles("Customers").build();
		
		Authentication auth = new UsernamePasswordAuthenticationToken(email, null,user.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		model.addAttribute("message", "Đăng nhập MXH thành công!");
		return "security/login";
	}
	
	@GetMapping("/oauth2/login/error")
	public String error(Model model) {
		model.addAttribute("message", "Đăng nhập MXH không thành công!");
		return "security/login";
	}
	
	
	

}
