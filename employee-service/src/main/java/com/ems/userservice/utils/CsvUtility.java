package com.ems.userservice.utils;

import com.ems.userservice.dto.UserDTO;
import com.ems.userservice.exceptions.InvalidNumberFormatException;
import com.ems.userservice.exceptions.UniqueLoginException;
import com.ems.userservice.exceptions.UserAlreadyExistsException;
import com.ems.userservice.model.User;
import com.ems.userservice.service.UserServiceInterface;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class CsvUtility {

    private static Map<String, String> usersToBeValidated;



    public static List<User> csvToList(String filePath) throws IOException {

            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Path.of(filePath))))) {

                // create csv bean reader
                CsvToBean<UserDTO> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(UserDTO.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();
                usersToBeValidated = new HashMap<>();
                List<UserDTO> userDTOS = csvToBean.parse();
                reader.close();
                Predicate<UserDTO> userDTOInvalidCheck = (userDto) -> isInvalidUserRecord(userDto);
                boolean inValidUserRecords = userDTOS.stream().anyMatch(userDTOInvalidCheck);

                List<User> users = null;
                if(!inValidUserRecords){
                    users = userDTOS.stream().filter(userDTO -> !userDTO.getId().contains("#")).map((userDTO) -> {
                        User user = DateTransformerUtility.convertUserDTOtoUser(userDTO, new User());
                        user.setActionBy("System");
                        user.setIsImported(1);
                        return user;
                    }).collect(Collectors.toList());
                }
                return users;
            } catch (Exception ex){
                throw ex;
            }
    }

    private static boolean isInvalidUserRecord(UserDTO userDTO) {
        if(!userDTO.getId().contains("#")){
            if(usersToBeValidated.get(userDTO.getId()) != null){
                throw new UserAlreadyExistsException(userDTO.getId());
            } else if(isNotAUniqueLogin(userDTO.getLogin(), userDTO.getId())){
                throw new UniqueLoginException(userDTO.getLogin());
            } else if(!NumberUtils.isCreatable(userDTO.getSalary()) || (Double.parseDouble(userDTO.getSalary()) <= 0)){
                throw new InvalidNumberFormatException(userDTO.getSalary());
            }
            usersToBeValidated.put(userDTO.getId(), userDTO.getLogin());
        }
        return false;
    }

    private static boolean isNotAUniqueLogin(String login, String id) {

        return usersToBeValidated.entrySet().stream().anyMatch(entry -> {
                if(entry.getValue().equals(login) && !entry.getKey().equals(id)){
                    return true;
                }
            return false;
        });
    }
}
