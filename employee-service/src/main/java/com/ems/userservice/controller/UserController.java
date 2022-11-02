package com.ems.userservice.controller;

import com.ems.userservice.constants.SortOrder;
import com.ems.userservice.dto.UserDTO;
import com.ems.userservice.exceptions.UserNotFoundException;
import com.ems.userservice.model.User;
import com.ems.userservice.response.SuccessResponseList;
import com.ems.userservice.response.SuccessResponseMessage;
import com.ems.userservice.service.StorageServiceInterface;
import com.ems.userservice.service.UserServiceInterface;
import com.ems.userservice.utils.CsvUtility;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private StorageServiceInterface storageServiceInterface;

    // get all users
    @GetMapping()
    public ResponseEntity<SuccessResponseList> getAllUsers(@RequestParam(required = false, defaultValue = "0.00") Double minSalary,
                                                           @RequestParam(required = false, defaultValue = "4000.00") Double maxSalary,
                                                           @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                           @RequestParam(required = false, defaultValue = "0") Integer limit,
                                                           @RequestParam(required = false, defaultValue = "id") String sortField,
                                                           @RequestParam(required = false, defaultValue = "ASC") String sortOrder){

        return ResponseEntity.ok(userServiceInterface.findAll(minSalary, maxSalary, offset, limit, sortField, sortOrder));
    }

    // get specific user
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserDetail(@PathVariable String id){
        User user = userServiceInterface.findOne(id);
        return ResponseEntity.ok(user);
    }

    // save new user
    @PostMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(userServiceInterface.save(userDTO));
    }


    // update existing user
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponseMessage> updateUser(@PathVariable String id, @Valid @RequestBody UserDTO userDTO) {
        userServiceInterface.updateUser(id, userDTO);
        return new ResponseEntity(new SuccessResponseMessage("Successfully updated"), HttpStatus.OK);
    }

    // delete specific user
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponseMessage> deleteUser(@PathVariable String id){
        userServiceInterface.deleteById(id);
        return new ResponseEntity(new SuccessResponseMessage("Successfully deleted!"), HttpStatus.OK);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponseMessage> uploadEmployeesData(@RequestParam("file") MultipartFile file) throws IOException {

        // upload csv file to server
        String destinationPath = storageServiceInterface.uploadFile(file);
        // Get users data from CSV
        List<User> users = CsvUtility.csvToList(destinationPath);

        // Save users data in DB
        if(users != null){
            userServiceInterface.saveAll(users);
            return new ResponseEntity(new SuccessResponseMessage("Data created or uploaded"), HttpStatus.OK);
        }
        return new ResponseEntity(new SuccessResponseMessage("Invalid employee data"), HttpStatus.BAD_REQUEST);

    }

}
