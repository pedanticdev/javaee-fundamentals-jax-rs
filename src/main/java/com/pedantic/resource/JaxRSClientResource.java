package com.pedantic.resource;

import com.pedantic.entities.PawnedDomains;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("client")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JaxRSClientResource {

    @Inject
    private JaxrsClient jaxrsClient;


    @GET
    @Path("pawned")
    public Response haveIBeenPawned(@QueryParam("account") @NotNull String account) {

        return Response.ok(jaxrsClient.getBreaches(account)).build();
    }
}
