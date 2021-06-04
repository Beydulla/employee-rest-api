package com.getwhelp.employeerest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getwhelp.employeerest.dto.response.ErrorResponse;
import com.getwhelp.employeerest.exception.RecordNotFoundException;
import com.getwhelp.employeerest.model.Employee;
import com.getwhelp.employeerest.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeeRestController.class)
public class EmployeeRestControllerTest {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Create request should return bad request when employee name is less than 3")
    @Test
    public void postRequestWithInvalidName() throws Exception {
        final Employee employee = Employee.builder().name("Be").surname("Zeynalov").salary(new BigDecimal("1.0")).build();
        final MethodArgumentNotValidException exception = (MethodArgumentNotValidException)mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isBadRequest()).andReturn().getResolvedException();
        final String expectedErrorMessage = "name must be between 3 and 50 characters long!";
        final String actualErrorMessage = exception.getFieldError("name").getDefaultMessage();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @DisplayName("Create request should return bad request when employee surname is less than 3")
    @Test
    public void postRequestWithInvalidSurname() throws Exception {
        final Employee employee = Employee.builder().name("Beydulla").surname("Ze").salary(new BigDecimal("1.0")).build();
        final MethodArgumentNotValidException exception = (MethodArgumentNotValidException)mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isBadRequest()).andReturn().getResolvedException();
        final String expectedErrorMessage = "surname must be between 3 and 50 characters long!";
        final String actualErrorMessage = exception.getFieldError("surname").getDefaultMessage();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @DisplayName("Create request should return bad request when salary is less than 0.0")
    @Test
    public void postRequestWithInvalidSalary() throws Exception {
        final Employee employee = Employee.builder().name("Beydulla").surname("Zeynalov").salary(new BigDecimal("-1.0")).build();
        final MethodArgumentNotValidException exception = (MethodArgumentNotValidException)mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isBadRequest()).andReturn().getResolvedException();
        final String expectedErrorMessage = "salary should be greater than 0.0";
        final String actualErrorMessage = exception.getFieldError("salary").getDefaultMessage();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @DisplayName("Create request should return 201 when employee details is valid")
    @Test
    public void postRequestWithValidEmployee() throws Exception {
        final Employee inputEmployee = Employee.builder().name("Beydulla").surname("Zeynalov").salary(new BigDecimal("3333.0")).build();
        final Employee expectedEmployee = Employee.builder().id(1L).name("Beydulla").surname("Zeynalov").salary(new BigDecimal("3333.0")).build();
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(expectedEmployee);
        final ObjectMapper objectMapper = new ObjectMapper();
        final String actualEmployee = mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputEmployee)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(expectedEmployee), actualEmployee);
        verify(employeeService, times(1)).createEmployee(inputEmployee);
    }

    @DisplayName("Get all request should return 200 with list of employees")
    @Test
    public void getAllRequest() throws Exception {
        final List<Employee> employees = getDummyEmployeeList();
        when(employeeService.getAllEmployees()).thenReturn(employees);
        final String responseJson = mockMvc.perform(get("/employees")).andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();

        assertEquals(new ObjectMapper().writeValueAsString(employees), responseJson);
        verify(employeeService, times(1)).getAllEmployees();
    }

    @DisplayName("Should return 200 with employee specified by id")
    @Test
    public void getByIdRequest() throws Exception {
        final Employee expectedEmployee = Employee.builder().id(1L).name("Beydulla").surname("Zeynalov").salary(new BigDecimal("3333.0")).build();
        final long id = 1L;
        when(employeeService.getEmployeeById(id)).thenReturn(expectedEmployee);
        final String actualEmployeeJson = mockMvc.perform(get("/employees/" + id)).andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();

        assertEquals(new ObjectMapper().writeValueAsString(expectedEmployee), actualEmployeeJson);
        verify(employeeService, times(1)).getEmployeeById(id);
    }

    @DisplayName("Should return 404 when specified employee id not found")
    @Test
    public void getByIdNotFoundRequest() throws Exception {
        final long id = 1L;
        when(employeeService.getEmployeeById(id)).thenThrow(new RecordNotFoundException("Employee id not found: " + id));
        final String responseJson = mockMvc.perform(get("/employees/" + id))
                .andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
        final ErrorResponse expectError = ErrorResponse.builder().
                status(HttpStatus.NOT_FOUND.value()).message("Employee id not found: " + id).build();
        assertEquals(new ObjectMapper().writeValueAsString(expectError), responseJson);
        verify(employeeService, times(1)).getEmployeeById(id);
    }

    @DisplayName("Should return 204 when specified employee is deleted")
    @Test
    public void deleteByIdRequest() throws Exception {
        final long id = 1L;
        mockMvc.perform(delete("/employees/" + id)).andExpect(status().isNoContent());
        verify(employeeService, times(1)).deleteEmployee(id);
    }

    @DisplayName("Should return 404 when specified employee id not found")
    @Test
    public void deleteByIdNotFoundRequest() throws Exception {
        final long id = 1L;
        doThrow(new RecordNotFoundException("Employee Id not found: " + id)).when(employeeService).deleteEmployee(id);
        mockMvc.perform(delete("/employees/" + id)).andExpect(status().isNotFound());
        verify(employeeService, times(1)).deleteEmployee(id);
    }


    private List<Employee> getDummyEmployeeList(){
        final List<Employee> employees = new ArrayList<>();
        employees.add(Employee.builder().name("Beydulla").surname("Zeynalov").salary(new BigDecimal("3333.0")).build());
        employees.add(Employee.builder().name("Name 2").surname("Surname 2").salary(new BigDecimal("2222.0")).build());
        employees.add(Employee.builder().name("Name 3").surname("Surname 3").salary(new BigDecimal("4444.0")).build());
        employees.add(Employee.builder().name("Name 4").surname("Surname 4").salary(new BigDecimal("5555.0")).build());
        return employees;
    }

}
