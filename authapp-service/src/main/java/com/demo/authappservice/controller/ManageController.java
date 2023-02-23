package com.demo.authappservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.authappservice.entity.LoadData;
import com.demo.authappservice.service.ManageService;

@CrossOrigin("*")
@RestController
public class ManageController {

	@Autowired
	private ManageService manageService;
	
	@GetMapping("/api/loaddata")
	public List<String> loadDataType(String datatype) {
		return manageService.loadDataType(datatype);
	}
	
}
