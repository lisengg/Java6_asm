package com.poly.bean;



import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Data
@Entity 
@Table(name = "Accounts")
@Getter
@Setter
public class Account  implements Serializable{
	
	@Id
	 @NotEmpty(message = "Vui lòng nhập tên người dùng")
	String username;
	 @NotEmpty(message = "Vui lòng nhập mật khẩu")
	 @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
	String password;
	@NotBlank(message = "Không để trống họ tên")
	String fullname;
	@NotBlank(message = "Không để trống email")
	@Email(message = "Email không đúng định dạng")
	String email;
	String photo;
	@JsonIgnore
	@OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
	List<Authority> authorities;
}
