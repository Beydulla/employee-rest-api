package com.getwhelp.employeerest.service;

import com.getwhelp.employeerest.exception.RecordNotFoundException;
import com.getwhelp.employeerest.model.Employee;
import com.getwhelp.employeerest.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository repository;

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @Override
    public Employee getEmployeeById(final long id) {
        return repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Employee id not found: " + id));
    }

    @Override
    public Employee createEmployee(final Employee employee) {
        return repository.save(employee);
    }

    @Override
    public Employee updateOrCreateEmployee(final long id, final Employee employee){
        employee.setId(id);
        return repository.save(employee);
    }

    @Override
    public void deleteEmployee(final long id) {
        try{
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException exception){
            throw new RecordNotFoundException("Employee Id not found: " + id);
        }
    }

    @Override
    public Page<Employee> pagination(final int pageNum, final int pageSize, final String sortBy,
                                final String sortDir, final String filter){
        final Sort sort = "asc".equals(sortDir) ? Sort.by(sortBy) : Sort.by(sortBy).descending();
        final Pageable pageable = PageRequest.of(pageNum, pageSize,sort);

        return "".equals(filter) ? repository.findAll(pageable) :
                repository.findByNameContainingOrSurnameContaining(filter, filter, pageable);
    }
}
