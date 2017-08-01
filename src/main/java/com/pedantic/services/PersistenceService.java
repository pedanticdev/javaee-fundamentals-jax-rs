package com.pedantic.services;

import com.pedantic.entities.Department;
import com.pedantic.entities.Employee;
import com.pedantic.entities.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class PersistenceService {

    @Inject
    private EntityManager entityManager;
    @Inject
    private SecurityUtil securityUtil;

    public void saveEmployee(Employee employee) {
        if (employee.getId() == null) {
            entityManager.persist(employee);
        } else {
            entityManager.merge(employee);
        }
    }

    public void saveDepartment(Department department) {
        if (department.getId() == null) {
            entityManager.persist(department);
        } else {
            entityManager.merge(department);
        }
    }

    public void deleteDepartment(Department department) {
//        entityManager.remove(department);
        System.out.println("Department " + department.getName() + " deleted successfully");

    }

    public void saveUser(User user) {
        if (user.getId() == null) {
            user.setPassword(securityUtil.encodeText(user.getPassword()));
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }


}
