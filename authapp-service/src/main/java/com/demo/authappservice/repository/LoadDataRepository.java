package com.demo.authappservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.authappservice.entity.LoadData;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface LoadDataRepository extends CrudRepository<LoadData, Integer>{

	@Query("SELECT d FROM LoadData d WHERE d.datatype = :datatype")
	List<LoadData> loadDataType(@Param("datatype") String datatype);
}
