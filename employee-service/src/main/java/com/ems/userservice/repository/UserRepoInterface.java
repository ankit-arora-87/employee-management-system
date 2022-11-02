package com.ems.userservice.repository;

import com.ems.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepoInterface extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

}
