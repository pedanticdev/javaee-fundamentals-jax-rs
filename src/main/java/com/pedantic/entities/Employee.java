package com.pedantic.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlRootElement
@Entity
@NamedQuery(name = Employee.FIND_ALL_EMPLOYEESS, query = "SELECT e FROM Employee e ORDER BY e.name")
public class Employee implements Serializable {

    public static final String FIND_ALL_EMPLOYEESS = "findAllEmployees";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Version
    @Column(nullable = false)
    private Long version;

    @NotNull(message = "Employee name cannot be left empty")
    @Size(min = 3, max = 50, message = "Employee name must be between 3 and 50 characters")
    private String name;

    @NotNull(message = "Salary cannot be left empty")
    private BigDecimal salary;

    @NotNull
    @Size(min = 10, max = 10, message = "Social security number must be 10 characters long")
    private String ssn;

    @Lob
    private byte[] picture;

    @ManyToOne
    private Department department;

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
