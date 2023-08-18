package com.poly.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.poly.bean.OrderDetail;
import com.poly.bean.Product;
import com.poly.service.OrderDetailService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orderdetails")
public class OderdetailRestController {
	
	@Autowired
	OrderDetailService orderDetailService;
	
	@GetMapping()
	public List<OrderDetail> getAll() {
		return orderDetailService.findAll();
	}
	
	@GetMapping("{id}")
	public OrderDetail getOne(@PathVariable("id") Long id) {
		return orderDetailService.findById(id);
	}
	
	@PutMapping("{id}")
	public OrderDetail update(@PathVariable("id") Long id, @RequestBody OrderDetail orderDetail) {
		return orderDetailService.update(orderDetail);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Long id) {
		orderDetailService.delete(id);
	}
}
