package com.poly.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.bean.Role;
import com.poly.dao.RoleDAO;
import com.poly.service.RoleService;

@Service
public class RoleServiceimpl implements RoleService{
	@Autowired
	RoleDAO dao;
	
	public List<Role> findAll() {
		return dao.findAll();
	}
}
