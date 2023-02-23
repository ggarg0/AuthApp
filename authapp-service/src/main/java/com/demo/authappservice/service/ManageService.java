package com.demo.authappservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.authappservice.entity.LoadData;
import com.demo.authappservice.repository.LoadDataRepository;

@Service
public class ManageService {

	@Autowired
	private LoadDataRepository loadDataRepository;

	public List<String> loadDataType(String type) {
		return loadDataRepository.loadDataType(type).stream().map(LoadData::getDatavalue).collect(Collectors.toList());
	}
}
