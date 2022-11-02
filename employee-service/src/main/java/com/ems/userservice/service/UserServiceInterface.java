package com.ems.userservice.service;

import com.ems.userservice.dto.UserDTO;
import com.ems.userservice.model.User;
import com.ems.userservice.response.SuccessResponseList;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface UserServiceInterface {

    SuccessResponseList findAll(Double minSalary, Double maxSalary, Integer page, Integer limit, String sortField, String sortOrder);

    User findOne(String id);

    User save(UserDTO userDTO);

    void saveAll(List<User> users);

    void deleteById(String id);

    User updateUser(String id, UserDTO userDTO);

    boolean isNotAUniqueLogin(String login, String id);
}
