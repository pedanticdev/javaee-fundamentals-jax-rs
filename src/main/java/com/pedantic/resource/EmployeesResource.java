package com.pedantic.resource;

import com.pedantic.entities.Employee;
import com.pedantic.entities.EmployeeBeanParam;
import com.pedantic.services.PersistenceService;
import com.pedantic.services.QueryService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("/employees")
@Produces("application/json")
@Consumes("application/json")
public class EmployeesResource {

    @Inject
    private QueryService queryService;
    @Inject
    private PersistenceService persistenceService;
    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;

    @GET
    public List<Employee> getEmployees() {

        return queryService.getEmployees();
    }

    @POST
    public Response saveEmployee(@Valid Employee employee) {
        persistenceService.saveEmployee(employee);

        NewCookie cookie = new NewCookie("employeeId", employee.getId().toString());
        NewCookie cookie1 = new NewCookie("employeeName", employee.getName());

        return Response.created(URI.create(uriInfo.getAbsolutePath() + "/" + employee.getId())).
                cookie(cookie).cookie(cookie1).build();
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
    public Response getEmployeeQueryParam(@QueryParam("id") Long id) {
        Employee employee = queryService.getEmployeeById(id);

        return Response.ok().entity(employee).build();
    }

    @GET
    @Path("{id}")
    public Employee getEmployeeById(@PathParam("id") Long id) {
        return queryService.getEmployeeById(id);
    }

    @POST
    @Path("form")
    public void createEmployeeBeanParam(@BeanParam EmployeeBeanParam beanParam) {
        System.out.println(beanParam.getName());
        System.out.println(beanParam.getSalary());
        System.out.println(beanParam.getSsn());
        System.out.println(beanParam.getMyHeader());
        System.out.println(beanParam.getId());
    }
}
