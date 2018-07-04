package com.pedantic.resource;

import com.pedantic.entities.Employee;
import com.pedantic.services.PersistenceService;
import com.pedantic.services.QueryService;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Path("employees")
@Produces("application/json")
@Consumes("application/json")
public class EmployeesResource {

    @Inject
    private JaxrsClient jaxrsClient;

    @Inject
    private QueryService queryService;
    @Inject
    private PersistenceService persistenceService;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;

    @GET //http://localhost:8080/jax-rs/api/v1/employees -GET
    public List<Employee> getEmployees() {

        return queryService.getEmployees();
    }

    @POST
    public Response saveEmployee(@Valid Employee employee) {
        persistenceService.saveEmployee(employee);
//        jaxrsClient.postEmployeeToSSE(employee);

//        NewCookie cookie = new NewCookie("employeeId", employee.getId().toString());
//        NewCookie cookie1 = new NewCookie("employeeName", employee.getName());


        URI uri = uriInfo.getAbsolutePathBuilder().path(employee.getId().toString()).build();

        URI others = uriInfo.getBaseUriBuilder().path(EmployeesResource.class).build();

//        URI dept = uriInfo.getBaseUriBuilder().path(DepartmentResource.class).path(DepartmentResource.class, "getDepartmentById")
//                .resolveTemplate("id", employee.getDepartment().getId()).build();

        JsonObjectBuilder links = Json.createObjectBuilder().add("_links", Json.createArrayBuilder().add(Json.createObjectBuilder().add("_others", others.toString())
                .add("_self", uri.toString()).build()
        ));


        return Response.ok(links.build().toString()).status(Response.Status.CREATED).build();
    }

    //Server content negotiation
    @GET
    @Path("/find/{id}")
    @Produces({"application/xml; qs=0.75", "application/json; qs=1.0"})
    public Response getEmployeesResponse(@PathParam("id") Long id) {
        Employee employee = queryService.getEmployeeById(id);
        if (employee == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.ok(queryService.getEmployeeById(id)).build();
    }

//    @GET
//    public Response getEmployeeQueryParam(@QueryParam("id") Long id) {
//        Employee employee = queryService.getEmployeeById(id);
//
//        return Response.ok().entity(employee).build();
//    }
    @GET
    @Path("{id}")
    public Employee getEmployeeById(@PathParam("id") Long id) {
        return queryService.getEmployeeById(id);
    }

    //Accepting other input types apart from Json or xml
    @POST
    @Path("pic")
    @Consumes({MediaType.APPLICATION_OCTET_STREAM, "image/png", "image/jpg"})
    @Produces(MediaType.TEXT_PLAIN)
    public Response postEmployeePicture(File picture, @QueryParam("id") @NotNull Long id) {

        //Associate the file with the selected employee and perhaps do something with it. Like DB storage
        Employee employee = queryService.getEmployeeById(id);

        try (Reader reader = new FileReader(picture)) {

            employee.setPicture(Files.readAllBytes(Paths.get(picture.toURI())));
            persistenceService.saveEmployee(employee);

            int totalsize = 0;
            int count = 0;
            final char[] buffer = new char[256];
            while ((count = reader.read(buffer)) != -1) {
                totalsize += count;
            }
            return Response.ok(totalsize).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    //Producing other output types apart from json or xml
    @GET
    @Path("employee-pic")
    @Produces({MediaType.APPLICATION_OCTET_STREAM, "image/png", "image/jpg"})
    public Response getEmployeePicture(@QueryParam("id") @NotNull Long id) throws Exception {
        Employee employee = queryService.getEmployeeById(id);
        if (employee != null) {
            return Response.ok().entity(Files.write(Paths.get("pic.png"), employee.getPicture()).toFile()).build();
        }

        return Response.noContent().build();
    }

}
