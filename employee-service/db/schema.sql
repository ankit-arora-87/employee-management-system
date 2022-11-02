-- phpMyAdmin SQL Dump
-- version 4.9.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: Oct 29, 2022 at 07:19 AM
-- Server version: 5.7.32
-- PHP Version: 7.4.12

--SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
--SET time_zone = "+00:00";

--
-- Database: `user_management_system`
--
-- CREATE DATABASE IF NOT EXISTS
CREATE SCHEMA IF NOT EXISTS employee_management_system;
-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` varchar(50) NOT NULL,
  `login` varchar(50) NOT NULL,
  `name` varchar(255) NOT NULL,
  `salary` decimal(19,4) NOT NULL,
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_imported` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0=> No, 1 => Yes',
  `action_by` varchar(100) NOT NULL DEFAULT 'User' COMMENT 'System => By Import Action, User => By Specific User'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `users_audit_logs`
--

CREATE TABLE `users_audit_logs` (
  `user_audit_log_id` int(11) NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `login` varchar(50) NOT NULL,
  `name` varchar(255) NOT NULL,
  `salary` decimal(19,4) NOT NULL,
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_imported` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0=> No, 1 => Yes',
  `action_by` varchar(100) NOT NULL DEFAULT 'User' COMMENT 'System => By Import Action, User => By Specific User'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
--ALTER TABLE `users`
--  ADD PRIMARY KEY (`id`),
--  ADD UNIQUE KEY `login_unique_idx` (`login`),
--  ADD KEY `salary_idx` (`salary`),
--  ADD KEY `start_date_idx` (`start_date`),
--  ADD KEY `action_by_idx` (`action_by`);
--ALTER TABLE `users` ADD FULLTEXT KEY `name_idx` (`name`);

--
-- Indexes for table `users_audit_logs`
--
--ALTER TABLE `users_audit_logs`
--  ADD PRIMARY KEY (`user_audit_log_id`),
--  ADD KEY `ual_salary_idx` (`salary`),
--  ADD KEY `ual_start_date_idx` (`start_date`),
--  ADD KEY `ual_id_idx` (`id`) USING BTREE,
--  ADD KEY `ual_login_unique_idx` (`login`) USING BTREE,
--  ADD KEY `action_by_idx` (`action_by`);
--ALTER TABLE `users_audit_logs` ADD FULLTEXT KEY `ual_name_idx` (`name`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users_audit_logs`
--
ALTER TABLE `users_audit_logs`
  MODIFY `user_audit_log_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `users_audit_logs`
--
ALTER TABLE `users_audit_logs`
  ADD CONSTRAINT `user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

