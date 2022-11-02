package com.ems.userservice.specification;

import com.ems.userservice.model.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification {

    public static Specification<User> betweenSalaryRange(Double minSalary, Double maxSalary) {
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate salaryPredicate = cb.between(root.get("salary"), minSalary, maxSalary-1);
                return salaryPredicate;
            }
        };
    }

    public static Specification<User> userWithSameLogin(String login, String userId) {
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate sameLogin = cb.equal(root.get("login"),login);
                Predicate differentUserId = cb.notEqual(root.get("id"), userId);
                return cb.and(sameLogin, differentUserId);
            }
        };
    }




}
