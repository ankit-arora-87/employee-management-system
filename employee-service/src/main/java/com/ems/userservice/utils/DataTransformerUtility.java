package com.ems.userservice.utils;

import com.ems.userservice.dto.UserDTO;
import com.ems.userservice.exceptions.InvalidDateParseException;
import com.ems.userservice.exceptions.InvalidNumberFormatException;
import com.ems.userservice.model.User;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Component
public class DataTransformerUtility {

    public static User convertUserDTOtoUser(UserDTO userDTO, User user){
        transformStartDateField(userDTO, user);
        transformSalaryField(userDTO, user);
        return user;
    }

    private static void transformSalaryField(UserDTO userDTO, User user) {
         if(!NumberUtils.isCreatable(userDTO.getSalary()) || (Double.parseDouble(userDTO.getSalary()) <= 0)) {
             throw new InvalidNumberFormatException(userDTO.getSalary());
         }
        user.setSalary(Double.parseDouble(userDTO.getSalary()));
    }

    private static void transformStartDateField(UserDTO userDTO, User user) {
        try {
            if(userDTO.getStartDate().split("-")[1].length() > 2){
                DateTimeFormatter df = new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("dd-MMM-yy")
                        .toFormatter(Locale.ENGLISH);
                userDTO.setStartDate(LocalDate.parse(userDTO.getStartDate(), df).toString());
            }
            BeanUtils.copyProperties(userDTO, user);
            user.setStartDate(LocalDate.parse(userDTO.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            } catch (DateTimeParseException ex) {
                throw new InvalidDateParseException(userDTO.getStartDate());
            }
    }
}
