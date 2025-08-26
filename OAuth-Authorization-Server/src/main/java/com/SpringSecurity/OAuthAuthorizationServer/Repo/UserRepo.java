package com.SpringSecurity.OAuth_Authorization_Server.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.SpringSecurity.OAuth_Authorization_Server.entiry.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{

	User findByEmail(String email);

}