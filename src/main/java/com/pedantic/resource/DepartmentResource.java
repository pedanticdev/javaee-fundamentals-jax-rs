package com.pedantic.resource;

import com.pedantic.config.MaxAge;
import com.pedantic.entities.Department;
import com.pedantic.services.PersistenceService;
import com.pedantic.services.QueryService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@Path("departments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentResource {

    @Inject
    private PersistenceService persistenceService;
    @Inject
    private QueryService queryService;
    @Context
    private UriInfo uriInfo;

    @POST
    public Response saveDepartment(Department department) {
        persistenceService.saveDepartment(department);
        return Response.created(URI.create(uriInfo.getAbsolutePath() + "/" + department.getId().toString()))
                .build();
    }

    @Path("{id}")
    @GET
    public Response getDepartmentById(@PathParam("id") Long id) {
        Department department = queryService.getDepartmentById(id);
        if (department != null) {
            return Response.ok().entity(department).build();
        }
        return Response.noContent().build();
    }



    @GET
    @MaxAge(value = 3600)
    public List<Department> getDepartments() {
        List<Department> departments = new ArrayList<>();
        departments.add(new Department("Finance and Admin"));
        departments.add(new Department("Human Resources"));
        departments.add(new Department("General Services"));
        departments.add(new Department("Production"));
        departments.add(new Department("Quality Assurance"));
        return departments;
    }
}
