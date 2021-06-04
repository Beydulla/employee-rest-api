package com.getwhelp.employeerest.controller;

import com.getwhelp.employeerest.model.Employee;
import com.getwhelp.employeerest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("employees")
public class EmployeeRestController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAll(){
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable final long id){
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody @Valid final Employee employee){
        return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable final long id, @RequestBody @Valid final Employee employee){
        return new ResponseEntity<>(employeeService.updateOrCreateEmployee(id, employee), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> delete(@PathVariable final long id){
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Employee>> pageResponse
            (@RequestParam(defaultValue = "0") final int pageNum, @RequestParam(defaultValue = "10") final int pageSize,
             @RequestParam(defaultValue = "name") final String sortBy, @RequestParam(defaultValue = "asc") final String sortDir,
             @RequestParam(defaultValue = "") final String filter){
        return new ResponseEntity<>(employeeService.pagination(pageNum, pageSize, sortBy, sortDir, filter), HttpStatus.OK);
    }

}
