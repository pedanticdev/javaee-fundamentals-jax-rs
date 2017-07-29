package com.pedantic.resource;

import com.pedantic.config.MaxAge;
import com.pedantic.entities.Department;
import com.pedantic.services.PersistenceService;
import com.pedantic.services.QueryService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;


@Path("departments")
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
    @MaxAge(value = 3600)
    public Response getDepartmentById(@PathParam("id") Long id) {
        Department department = queryService.getDepartmentById(id);
        if (department != null) {
            return Response.ok().entity(department).build();
        }
        return Response.noContent().build();
    }
}
