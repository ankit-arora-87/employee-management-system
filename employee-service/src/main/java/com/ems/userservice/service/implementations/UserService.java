package com.ems.userservice.service.implementations;

import com.ems.userservice.constants.SortOrder;
import com.ems.userservice.dto.UserDTO;
import com.ems.userservice.exceptions.UniqueLoginException;
import com.ems.userservice.exceptions.UserAlreadyExistsException;
import com.ems.userservice.exceptions.UserNotFoundException;
import com.ems.userservice.model.User;
import com.ems.userservice.repository.UserRepoInterface;
import com.ems.userservice.response.SuccessResponseList;
import com.ems.userservice.service.UserServiceInterface;
import com.ems.userservice.specification.UserSpecification;
import com.ems.userservice.utils.DataTransformerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    private UserRepoInterface userRepoInterface;

    @Override
    public SuccessResponseList findAll(Double minSalary, Double maxSalary, Integer page, Integer limit, String sortField, String sortOrder) {
        Sort sortByOrder = Sort.by(sortField);
        if(sortOrder.equalsIgnoreCase(SortOrder.ASC.name())){
            sortByOrder = sortByOrder.ascending();
        } else {
            sortByOrder = sortByOrder.descending();
        }

        Specification<User> specSalaryRange = UserSpecification.betweenSalaryRange(minSalary, maxSalary);
        Specification whereSpec  = Specification.where(specSalaryRange);

        if(limit == 0){
            limit = Math.toIntExact(userRepoInterface.count());
        }
        Pageable pageable = PageRequest.of(page, limit, sortByOrder);
        Page<User> usersPage = userRepoInterface.findAll( whereSpec, pageable);
        return new SuccessResponseList(usersPage.getContent(), page, usersPage.getTotalPages(), (int) usersPage.getTotalElements());
    }

    @Override
    public User findOne(String id) {
        Optional<User> userOptional = userRepoInterface.findById(id);
        if (userOptional.isEmpty())
            throw new UserNotFoundException(id);

        return userOptional.get();
    }

    @Override
    public User save(UserDTO userDTO) {

        User user = DataTransformerUtility.convertUserDTOtoUser(userDTO, new User());

        Optional<User> userOptional = userRepoInterface.findById(user.getId());
        if (userOptional.isPresent()) {
            throw new UserAlreadyExistsException(user.getId());
        } else if (isNotAUniqueLogin(userDTO.getLogin(), userDTO.getId())){
            throw new UniqueLoginException(user.getLogin());
        }
        return userRepoInterface.save(user);
    }
    @Override
    public void saveAll(List<User> users) {
        userRepoInterface.saveAll(users);
    }

    @Override
    public User updateUser(String id, UserDTO userDTO) {

        User user = DataTransformerUtility.convertUserDTOtoUser(userDTO, new User());

        Optional<User> userOptional = userRepoInterface.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        else if (isNotAUniqueLogin(userDTO.getLogin(), userDTO.getId())){
            throw new UniqueLoginException(user.getLogin());
        }
        user.setId(id);
        return userRepoInterface.save(user);
    }
    @Override
    public void deleteById(String id) {
        Optional<User> userOptional = userRepoInterface.findById(id);
        if (userOptional.isEmpty())
            throw new UserNotFoundException(id);

        userRepoInterface.deleteById(id);
    }

    @Override
    public boolean isNotAUniqueLogin(String login, String id) {
        Specification<User> userWithSameLogin = UserSpecification.userWithSameLogin(login, id);
        Specification whereSpec  = Specification.where(userWithSameLogin);
        return userRepoInterface.findOne(whereSpec).isPresent();
    }
}
