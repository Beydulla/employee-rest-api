package com.getwhelp.employeerest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull(message = "name must not be null!")
    @Size(min = 3, max = 50, message = "name must be between 3 and 50 characters long!")
    private String name;

    @Column
    @NotNull(message = "surname must not be null!")
    @Size(min = 3, max = 50, message = "surname must be between 3 and 50 characters long!")
    private String surname;

    @Column
    @NotNull(message = "salary must not be null!")
    @DecimalMin(value = "0.0", inclusive = false, message = "salary should be greater than 0.0" )
    private BigDecimal salary;
}
