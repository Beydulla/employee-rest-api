package com.getwhelp.employeerest.service;

import com.getwhelp.employeerest.exception.RecordNotFoundException;
import com.getwhelp.employeerest.model.Employee;
import com.getwhelp.employeerest.repository.EmployeeRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;


    @DisplayName("getEmployeeById should throw RecordNotFoundException when specified employee id not found")
    @Test
    public void getEmployeeByIdNotFound(){
        final long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        final Throwable exception = assertThrows(RecordNotFoundException.class,
                () -> employeeService.getEmployeeById(id));
        final String expectedErrorMessage = "Employee id not found: " + id;
        assertEquals(expectedErrorMessage, exception.getMessage());
        verify(repository, times(1)).findById(id);
    }

    @DisplayName("getEmployeeById should return Employee when specified employee id found")
    @Test
    public void getEmployeeFound(){
        final long id = 1L;
        final Employee inputEmployee = Employee.builder().id(id).name("Beydulla").surname("Zeynalov").salary(new BigDecimal("3333.0")).build();
        when(repository.findById(id)).thenReturn(Optional.ofNullable(inputEmployee));
        final Employee actualEmployee = employeeService.getEmployeeById(id);
        assertEquals(inputEmployee, actualEmployee);
        verify(repository, times(1)).findById(id);
    }

    @DisplayName("deleteEmployee should throw RecordNotFoundException when specified employee id not found")
    @Test
    public void deleteEmployeeByIdNotFound(){
        final long id = 1L;
        doThrow(new EmptyResultDataAccessException(0)).when(repository).deleteById(id);
        final Throwable exception = assertThrows(RecordNotFoundException.class,
                () -> employeeService.deleteEmployee(id));
        final String expectedErrorMessage = "Employee Id not found: " + id;
        assertEquals(expectedErrorMessage, exception.getMessage());
        verify(repository, times(1)).deleteById(id);
    }

    @DisplayName("pagination should call findByNameContainingOrSurnameContaining() when filter is not empty")
    @Test
    public void paginationWithFilter(){
        final String filter = "beydu";
        employeeService.pagination(1, 1, "name", "asc", filter);
        verify(repository, times(1)).findByNameContainingOrSurnameContaining(any(), any(), any());
        verify(repository, times(0)).findAll(any(Pageable.class));
    }

    @DisplayName("pagination should call findAll() when filter is empty")
    @Test
    public void paginationWithEmptyFilter(){
        final String filter = "";
        employeeService.pagination(1, 1, "name", "asc", filter);
        verify(repository, times(0)).findByNameContainingOrSurnameContaining(any(), any(), any());
        verify(repository, times(1)).findAll(any(Pageable.class));
    }
}
