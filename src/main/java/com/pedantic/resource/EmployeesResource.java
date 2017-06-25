package com.pedantic.resource;

import com.pedantic.entities.Employee;
import com.pedantic.services.QueryService;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("/employees")
@Produces("application/json")
@Consumes("application/json")
public class EmployeesResource {

    @Inject
    private QueryService queryService;

    @GET
    public List<Employee> getEmployees() {
        return queryService.getEmployees();
    }

    @GET
    @Path("{id}")
    public Employee getEmployeeById(@PathParam("id") Long id) {
        return queryService.getEmployeeById(id);
    }
}
