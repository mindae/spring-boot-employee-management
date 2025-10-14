package com.example.hello.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hello.model.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	
	List<Authority> findByUsername(String username);
}
