package com.pedantic.resource;

import com.pedantic.entities.Employee;
import com.pedantic.services.PersistenceService;
import com.pedantic.services.QueryService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/employees")
@Produces("application/json")
@Consumes("application/json")
public class EmployeesResource {

    @Inject
    private QueryService queryService;
    @Inject
    private PersistenceService persistenceService;

    @GET
    public List<Employee> getEmployees() {
      
        return queryService.getEmployees();
    }

    @POST
    public Response saveEmployee(@Valid Employee employee) {
        persistenceService.saveEmployee(employee);
        return Response.ok(employee).build();
    }

    @GET
    @Path("/find/{id}")
    public Response getEmployeesResponse(@PathParam("id") Long id) {
        Employee employee = getEmployeeById(id);
        if (employee == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(queryService.getEmployeeById(id)).build();
    }

    @GET
    @Path("{id}")
    public Employee getEmployeeById(@PathParam("id") Long id) {
        return queryService.getEmployeeById(id);
    }
}
