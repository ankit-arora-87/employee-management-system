package com.ems.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "ems_users", indexes = {
        @Index(name = "name_idx", columnList = "name"),
        @Index(name = "login_unique_idx", columnList = "login", unique = true),
        @Index(name = "salary_idx", columnList = "salary"),
        @Index(name = "start_date_idx", columnList = "startDate")
})
public class User {

    @Id
    @Column(nullable = false, length = 50, unique = true)
    @NotNull(message = "Please provide employee id")
    @Size(min = 3, max = 50, message = "Invalid employee id. Please provide at least 3 characters and max limit is up to 50 characters!")
    @CsvBindByName
    private String id;

    @Column(nullable = false, length = 50)
    @NotNull(message = "Please provide login")
    @Size(min = 3, max = 50, message = "Invalid login. Please provide at least 3 characters and max limit is up to 50 characters!")
    @CsvBindByName
    private String login;


    @Column(nullable = false, length = 255)
    @NotNull(message = "Please provide name")
    @Size(min = 3, max = 255, message = "Invalid name. Please provide at least 3 characters and max limit is up to 50 characters")
    @CsvBindByName
    private String name;

    @Column(nullable = false, precision = 19, scale = 4)
    @NotNull(message = "Please provide salary")
    @Positive(message = "Invalid salary. Salary must be greater than 0.0")
    @CsvBindByName
    private Double salary;

    @Column(nullable = false)
    @NotNull(message = "Please provide start date")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    @DateTimeFormat(fallbackPatterns = {"dd-mmm-yy", "yyyy-mm-dd"})
//    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @CsvBindByName
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonIgnore
    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Column(columnDefinition = "integer default 0 COMMENT '0 => No, 1 => Yes'", nullable = false)
    private Integer isImported = 0;

    @JsonIgnore
    @Column(columnDefinition = "varchar(100) default 'User' COMMENT 'System => By Import Action, User => By Specific User Action'", nullable = false )
    private String actionBy = "User";

    public User() {
    }

    public User(String id, String login, String name, Double salary, LocalDate startDate, Integer isImported, String actionBy) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.salary = salary;
        this.startDate = startDate;
        this.isImported = isImported;
        this.actionBy = actionBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate)  {this.startDate = startDate;}

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getIsImported() {
        return isImported;
    }

    public void setIsImported(Integer isImported) {
        this.isImported = isImported;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", startDate=" + startDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isImported=" + isImported +
                ", actionBy='" + actionBy + '\'' +
                '}';
    }
}
