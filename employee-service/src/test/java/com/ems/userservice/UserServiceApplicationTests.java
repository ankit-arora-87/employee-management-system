package com.ems.userservice;

import com.ems.userservice.model.User;
import com.ems.userservice.repository.UserRepoInterface;
import com.ems.userservice.response.SuccessResponseList;
import com.ems.userservice.response.ServiceResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepoInterface userRepository;

	private String EMPLOYEE_CSV_PATH = "<ABSOLUTE_PATH>/employee-management-system/employee-service/src/main/resources/uploads/employees-data.csv";
	//	eg: "/Users/ankitarora/projects/employee-management-system/employee-service/src/main/resources/uploads/employees-data.csv";

	@Test
	public void shouldReturnSuccessfullyImportOnFileUpload() throws Exception {

		this.mockMvc.perform(
						multipart("/api/v1/users/upload")
						.file(getEmployeeFile()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("Data created or uploaded")).hasJsonPath())
				.andReturn().getResponse().getContentAsString().equals(ServiceResponseMessage.class);
	}



	@Test
	public void shouldReturnUsersList() throws Exception {

		this.mockMvc.perform(
				multipart("/api/v1/users/upload")
						.file(getEmployeeFile()));

		this.mockMvc.perform(get("/api/v1/users?page=0&limit=5&sortField=login&sortOrder=ASC"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.results", List.of(User.class)).hasJsonPath())
				.andExpect(jsonPath("$.currentPage", 0).hasJsonPath())
				.andExpect(jsonPath("$.totalPages", is(Integer.class)).hasJsonPath())
				.andExpect(jsonPath("$.totalRecords", is(Integer.class)).hasJsonPath())
				.andReturn().getResponse().getContentAsString().equals(SuccessResponseList.class);
	}

	@Test
	public void shouldReturnUsersBlankList() throws Exception {
		this.mockMvc.perform(get("/api/v1/users?page=0&limit=5&sortField=login&sortOrder=ASC"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString().equals(SuccessResponseList.class);
	}

	@Test
	public void shouldReturnUserOnNewUserCreation() throws Exception {

		String userRequest = "{\"id\":\"e0011\",\"name\":\"Harry Potter\",\"login\":\"hpoter\",\"salary\":2500,\"startDate\":\"2011-11-16\"}";

		mockMvc.perform(post("/api/v1/users")
						.contentType("application/json")
						.content(userRequest))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString().equals(User.class);
	}

	@Test
	public void shouldReturnInvalidEmployeeIdOnNewUserCreation() throws Exception {

		this.mockMvc.perform(
				multipart("/api/v1/users/upload")
						.file(getEmployeeFile()));

		String userRequest = "{\"id\":\"e0001\",\"name\":\"Harry Potter\",\"login\":\"hpotter\",\"salary\":2500,\"startDate\":\"2011-11-16\"}";

		mockMvc.perform(post("/api/v1/users")
						.contentType("application/json")
						.content(userRequest))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Employee ID already exists - e0001")).hasJsonPath())
				.andReturn().getResponse().getContentAsString().equals(ServiceResponseMessage.class);
	}

	@Test
	public void shouldReturnInvalidLoginOnNewUserCreation() throws Exception {

		this.mockMvc.perform(
				multipart("/api/v1/users/upload")
						.file(getEmployeeFile()));

		String userRequest = "{\"id\":\"e0001\",\"name\":\"Harry Potter\",\"login\":\"hpotter\",\"salary\":2500,\"startDate\":\"2011-11-16\"}";

		mockMvc.perform(post("/api/v1/users")
						.contentType("application/json")
						.content(userRequest))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Employee login not unique - hpotter")).hasJsonPath())
				.andReturn().getResponse().getContentAsString().equals(ServiceResponseMessage.class);
	}

	@Test
	public void shouldReturnInvalidSalaryWith0OnNewUserCreation() throws Exception {

		String userRequest = "{\"id\":\"e0001\",\"name\":\"Harry Potter\",\"login\":\"hpotter\",\"salary\":0.0,\"startDate\":\"2011-11-16\"}";

		mockMvc.perform(post("/api/v1/users")
						.contentType("application/json")
						.content(userRequest))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Invalid salary - 0.0")).hasJsonPath())
				.andReturn().getResponse().getContentAsString().equals(ServiceResponseMessage.class);
	}

	@Test
	public void shouldReturnInvalidSalaryWithBadDataOnNewUserCreation() throws Exception {

		String userRequest = "{\"id\":\"e0001\",\"name\":\"Harry Potter\",\"login\":\"hpotter\",\"salary\":0.0.0,\"startDate\":\"2011-11-16\"}";

		mockMvc.perform(post("/api/v1/users")
						.contentType("application/json")
						.content(userRequest))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString().equals(ServiceResponseMessage.class);
	}

	@Test
	public void shouldReturnInvalidStartDateWithBadDataOnNewUserCreation() throws Exception {

		String userRequest = "{\"id\":\"e0001\",\"name\":\"Harry Potter\",\"login\":\"hpotter\",\"salary\":2500,\"startDate\":\"201-11-16\"}";

		mockMvc.perform(post("/api/v1/users")
						.contentType("application/json")
						.content(userRequest))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Invalid date format - 201-11-16")).hasJsonPath())
				.andReturn().getResponse().getContentAsString().equals(ServiceResponseMessage.class);
	}



	@Test
	public void shouldReturnUserOnUserUpdate() throws Exception {

		this.mockMvc.perform(
				multipart("/api/v1/users/upload")
						.file(getEmployeeFile()));

		String userRequest = "{\"id\":\"e0001\",\"name\":\"Harry Potter Updated\",\"login\":\"hpotter\",\"salary\":2550,\"startDate\":\"2011-11-16\"}";

		mockMvc.perform(put("/api/v1/users/e0001")
						.contentType("application/json")
						.content(userRequest))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("Successfully updated")).hasJsonPath())
				.andReturn().getResponse().getContentAsString().equals(ServiceResponseMessage.class);
	}

	@Test
	public void shouldReturnInvalidLoginOnUserUpdate() throws Exception {

		this.mockMvc.perform(
				multipart("/api/v1/users/upload")
						.file(getEmployeeFile()));

		String userRequest = "{\"id\":\"e0001\",\"name\":\"Harry Potter\",\"login\":\"rwesley\",\"salary\":2500,\"startDate\":\"2011-11-16\"}";

		mockMvc.perform(put("/api/v1/users/e0001")
						.contentType("application/json")
						.content(userRequest))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Employee login not unique - rwesley")).hasJsonPath())
				.andReturn().getResponse().getContentAsString().equals(ServiceResponseMessage.class);
	}

	@Test
	public void shouldReturnInvalidSalaryWith0OnUserUpdate() throws Exception {

		this.mockMvc.perform(
				multipart("/api/v1/users/upload")
						.file(getEmployeeFile()));

		String userRequest = "{\"id\":\"e0001\",\"name\":\"Harry Potter\",\"login\":\"hpotter\",\"salary\":0.0,\"startDate\":\"2011-11-16\"}";

		mockMvc.perform(put("/api/v1/users/e0001")
						.contentType("application/json")
						.content(userRequest))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Invalid salary - 0.0")).hasJsonPath())
				.andReturn().getResponse().getContentAsString().equals(ServiceResponseMessage.class);
	}

	@Test
	public void shouldReturnInvalidStartDateWithBadDataOnUserUpdate() throws Exception {

		this.mockMvc.perform(
				multipart("/api/v1/users/upload")
						.file(getEmployeeFile()));

		String userRequest = "{\"id\":\"e0001\",\"name\":\"Harry Potter\",\"login\":\"hpotter\",\"salary\":2500,\"startDate\":\"201-11-16\"}";

		mockMvc.perform(put("/api/v1/users/e0001")
						.contentType("application/json")
						.content(userRequest))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Invalid date format - 201-11-16")).hasJsonPath())
				.andReturn().getResponse().getContentAsString().equals(ServiceResponseMessage.class);
	}

	@Test
	public void shouldReturnSuccessfullyDeletedOnUserDeletion() throws Exception {

		String userRequest = "{\"id\":\"e0001\",\"name\":\"Harry Potter\",\"login\":\"hpotter\",\"salary\":2500,\"startDate\":\"2011-11-16\"}";

		mockMvc.perform(post("/api/v1/users").contentType("application/json")
				.content(userRequest));

		mockMvc.perform(delete("/api/v1/users/e0001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("Successfully deleted!")).hasJsonPath())
				.andReturn().getResponse().getContentAsString().equals(ServiceResponseMessage.class);
	}

	@Test
	public void shouldReturnNoUserFoundOnUserDeletion() throws Exception {

		mockMvc.perform(delete("/api/v1/users/e0001123"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("No such employee found for id - e0001")).hasJsonPath())
				.andReturn().getResponse().getContentAsString().equals(ServiceResponseMessage.class);
	}

	private MockMultipartFile getEmployeeFile() throws IOException {
		byte[] fileContent = Files.readAllBytes(Paths.get(EMPLOYEE_CSV_PATH));

		MockMultipartFile file
				= new MockMultipartFile(
				"file",
				"employees-data.csv",
				"text/csv",
				fileContent
		);
		return file;
	}

}