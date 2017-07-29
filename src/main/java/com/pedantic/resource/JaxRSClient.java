package com.pedantic.resource;

import com.pedantic.entities.Employee;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

public class JaxRSClient {

    private final Client CLIENT = ClientBuilder.newClient();


    public void testClient() {
        WebTarget target = CLIENT.target("http://localhost:8080/jax-rs/api/v1/employees/");

        Employee employee = new Employee();
        employee.setName("Luqman");
        employee.setSsn("1234567890");
        employee.setSalary(new BigDecimal("5000"));

        Response post = target.request().post(Entity.json(employee));
        post.close();

        Employee employee1 = target.queryParam("id", 1).request().get(Employee.class);

        System.out.println(employee1.getName());

        CLIENT.close();

    }
}
