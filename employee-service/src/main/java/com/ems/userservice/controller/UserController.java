package com.ems.userservice.controller;

import com.ems.userservice.dto.UserDTO;
import com.ems.userservice.model.User;
import com.ems.userservice.response.SuccessResponseList;
import com.ems.userservice.response.ServiceResponseMessage;
import com.ems.userservice.service.StorageServiceInterface;
import com.ems.userservice.service.UserServiceInterface;
import com.ems.userservice.utils.CsvUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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
                                                           @RequestParam(required = false, defaultValue = "0") Integer page,
                                                           @RequestParam(required = false, defaultValue = "0") Integer limit,
                                                           @RequestParam(required = false, defaultValue = "id") String sortField,
                                                           @RequestParam(required = false, defaultValue = "ASC") String sortOrder){

        return ResponseEntity.ok(userServiceInterface.findAll(minSalary, maxSalary, page, limit, sortField, sortOrder));
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
    public ResponseEntity<ServiceResponseMessage> updateUser(@PathVariable String id, @Valid @RequestBody UserDTO userDTO) {
        userServiceInterface.updateUser(id, userDTO);
        return new ResponseEntity(new ServiceResponseMessage("Successfully updated"), HttpStatus.OK);
    }

    // delete specific user
    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponseMessage> deleteUser(@PathVariable String id){
        userServiceInterface.deleteById(id);
        return new ResponseEntity(new ServiceResponseMessage("Successfully deleted!"), HttpStatus.OK);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServiceResponseMessage> uploadEmployeesData(@RequestParam("file") MultipartFile file) throws IOException {

        // upload csv file to server
        String destinationPath = storageServiceInterface.uploadFile(file);
        // Get users data from CSV
        List<User> users = CsvUtility.csvToList(destinationPath);

        // Save users data in DB
        if(users != null){
            userServiceInterface.saveAll(users);
            return new ResponseEntity(new ServiceResponseMessage("Data created or uploaded"), HttpStatus.OK);
        }
        return new ResponseEntity(new ServiceResponseMessage("Invalid employee data"), HttpStatus.BAD_REQUEST);

    }

}
