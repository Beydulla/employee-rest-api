package com.getwhelp.employeerest.repository;

import com.getwhelp.employeerest.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findByNameContainingOrSurnameContaining(final String filterName, final String filterSurname, final Pageable pageable);
}
