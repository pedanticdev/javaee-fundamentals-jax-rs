package com.pedantic.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@Entity
@NamedQuery(name = Department.FIND_ALL_DEPARTMENTS, query = "select d from Department d order by d.name")
public class Department implements Serializable {
    public static final String FIND_ALL_DEPARTMENTS = "findAllDepartments";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Version
    @Column(nullable = false)
    private Long version;

    private String name;

    public Department(String name) {
        this.name = name;
    }

    public Department() {
    }

    @ManyToOne
    private Department department;

    @OneToMany
    private final List<Employee> employees = new ArrayList<>();

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    public List<Employee> getEmployees() {
        return employees;
    }
}
