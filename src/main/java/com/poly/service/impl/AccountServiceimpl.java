package com.poly.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.bean.Account;
import com.poly.dao.AccountDAO;
import com.poly.service.AccountService;

@Service
public class AccountServiceimpl implements AccountService{
	@Autowired
	AccountDAO dao;

	@Override
	public Account findById(String username) {
		return dao.findById(username).get();
	}

	public List<Account> findAll() {
		return dao.findAll();
	}

	public List<Account> getAdministrators() {
		return dao.getAdministrators();
	}
}
