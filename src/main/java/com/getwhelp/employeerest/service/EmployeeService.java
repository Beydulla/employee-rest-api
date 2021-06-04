package com.getwhelp.employeerest.service;

import com.getwhelp.employeerest.model.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee getEmployeeById(final long id);

    Employee createEmployee(final Employee employee);

    Employee updateOrCreateEmployee(final long id, final Employee employee);

    void deleteEmployee(final long id);

    Page<Employee> pagination(final int pageNum, final int pageSize, final String sortBy,
                                final String sortDir, final String filter);

}
