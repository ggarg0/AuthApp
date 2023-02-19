package com.demo.authappservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.authappservice.entity.User;


@Repository
public interface AuthAppRepository extends CrudRepository<User, Integer> {
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
	User loadUserDetails(@Param("username") String username);
	
	@Query("SELECT d FROM LOADDATA d WHERE d.datatype = :datatype")
	List<String> loadDataType(@Param("datatype") String datatype);
	
	@Query("SELECT u FROM User u WHERE u.role = :role")
	List<User> loadUserByRole(@Param("role") String role);

	Optional<User> findByUsername(String username);
}


