package com.poly.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.bean.Account;
import com.poly.dao.AccountDAO;
@CrossOrigin("*")
@Controller
public class RegisterController {
	@Autowired
    private AccountDAO accountDAO;

    @GetMapping("/security/register/form")
    public String showRegistrationForm() {
        return "security/register"; // Tên của tệp HTML đăng ký
    }
	
	@PostMapping("/security/register")
	  public String registerUser(Model model, Account account,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String fullname,
            @RequestParam String email
    ) {
        // Kiểm tra dữ liệu đầu vào
        if (username.isEmpty() || password.isEmpty() || fullname.isEmpty() || email.isEmpty()) {
            model.addAttribute("message", "Vui lòng điền đầy đủ thông tin!");
            return "security/register"; 
        }

       // Kiểm tra định dạng email
        else if (!isValidEmail(email)) {
            model.addAttribute("message1", "Email không hợp lệ!");
            return "security/register"; 
        }else if(password.length()<6) {
        	 model.addAttribute("message","Mật khẩu có ít nhất 6 kí tự!");
        }
        else {
             accountDAO.save(account);
             model.addAttribute("message", "Đăng ký thành công!");
             // Trả về trang thành công hoặc trang lỗi
        }
       
       return "security/register"; 
   }

	private boolean isValidEmail(String email) {
		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
			        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		if(email.matches(regexPattern)) {
			return true;
		}
		return false;
	}


//	@PostMapping("/security/register")
//    public String processRegistration(Model model, Account account) {
//        // Lưu thông tin người dùng vào cơ sở dữ liệu
//
//	}
}
