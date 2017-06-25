package com.pedantic.services;

import com.pedantic.entities.Department;
import com.pedantic.entities.Employee;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Random;

@Singleton
@Startup
public class PopulateDatabase {

    

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    private void init() {
        final Random random = new Random();

        String[] names = {"Luqman Saeed", "Donald John Trump", "Barack Hussein Obama",
            "George Walker Bush", "Kofi Annan", "John Agekum Kuffour", "John Evans Fiffi Atta Mills",
            "John Dramani Mahama", "Nana Akuffo-Addo", "Jerry John Rawlings", "Robert Mugabe", "Nelson Mandela",
            "Jacque Chirac", "Olusegun Obasanjo"};
        String[] departments = {"Finace and Administration", "Accounts", "Human Resources", "General Services"};

        for (int i = 0; i < 500; i++) {
            Employee employee = new Employee();
            employee.setName(names[random.nextInt(names.length)]);
            employee.setSsn(names[random.nextInt(names.length)].substring(random.nextInt(5)));
            employee.setSalary(new BigDecimal(random.nextInt(4500)));

            Department department = new Department();
            department.setName(departments[random.nextInt(departments.length)]);

            employee.setDepartment(department);
            department.getEmployees().add(employee);

            em.persist(employee);
            em.persist(department);

        }

    }
}
