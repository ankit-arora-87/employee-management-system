package com.ems.userservice.response;

import com.ems.userservice.model.User;

import java.util.List;

public class SuccessResponseList {

    private List<User> results;

    private int currentPage;
    private int totalPages;
    private int totalRecords;
    public SuccessResponseList(List<User> results) {
        this.results = results;
    }

    public SuccessResponseList(List<User> results, int currentPage, int totalPages, int totalRecords) {
        this.results = results;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalRecords = totalRecords;
    }

    public List<User> getResults() {
        return results;
    }

    public void setResults(List<User> results) {
        this.results = results;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
}
