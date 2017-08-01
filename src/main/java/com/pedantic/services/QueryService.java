package com.pedantic.services;

import com.pedantic.entities.Department;
import com.pedantic.entities.Employee;
import com.pedantic.entities.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@Stateless
public class QueryService {
    @Inject
    private EntityManager entityManager;
    @Inject
    private SecurityUtil securityUtil;


    public Employee getEmployeeById(Long id) {
        return entityManager.find(Employee.class, id);
    }

    public Department getDepartmentById(Long id) {
        return entityManager.find(Department.class, id);
    }

    public List<Employee> getEmployees() {
        return entityManager.createNamedQuery(Employee.FIND_ALL_EMPLOYEESS, Employee.class).getResultList();
    }

    public List<Department> getDepartments() {
        return entityManager.createNamedQuery(Department.FIND_ALL_DEPARTMENTS, Department.class).getResultList();
    }
    public User findUserByEmail(String email) {
        return entityManager.createNamedQuery(User.FIND_BY_EMAIL, User.class).setParameter("email", email).getSingleResult();
    }

    public User findUserByCredentials(String email, String plainTextPassword) {
        return entityManager.createNamedQuery(User.FIND_BY_CREDENTIALS, User.class).setParameter("email", email)
                .setParameter("password", securityUtil.encodeText(plainTextPassword)).getSingleResult();
    }

    public User findUserById(Long id) {
        return entityManager.find(User.class, id);
    }

}
