package com.poly.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.bean.OrderDetail;
import com.poly.dao.OrderDAO;
import com.poly.dao.OrderDetailDAO;
import com.poly.service.OrderDetailService;


@Service
public class OrderDetailServiceimpl implements OrderDetailService{
	@Autowired
	OrderDAO dao;
	
	@Autowired
	OrderDetailDAO ddao;

	@Override
	public List<OrderDetail> findAll() {
		return ddao.findAll();
	}

	@Override
	public OrderDetail findById(Long id) {
		return ddao.findById(id).get();
	}

	@Override
	public void delete(Long id) {
		ddao.deleteById(id);	
	}

	@Override
	public OrderDetail update(OrderDetail orderDetail) {
		return ddao.save(orderDetail);
	}

	



}
