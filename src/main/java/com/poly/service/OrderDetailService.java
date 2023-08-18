package com.poly.service;

import java.util.List;

import com.poly.bean.OrderDetail;

public interface OrderDetailService {

	List<OrderDetail> findAll();

	OrderDetail findById(Long id);

	void delete(Long id);

	OrderDetail update(OrderDetail orderDetail);

	


}
