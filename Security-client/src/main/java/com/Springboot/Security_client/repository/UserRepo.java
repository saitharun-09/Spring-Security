package com.Springboot.Security_client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Springboot.Security_client.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{

	User findByEmail(String email);

}
