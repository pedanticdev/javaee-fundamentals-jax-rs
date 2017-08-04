package com.pedantic.resource;

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

@Path("client")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JaxRSClientResource {

    private final Client CLIENT = ClientBuilder.newClient();
    private static final String HAVE_I_BEEN_PAWNED_API = "https://haveibeenpwned.com/api/v2/breachedaccount/{account}";

    @GET
    @Path("pawned")
    public Response haveIbeenPawned(@QueryParam("account") @NotNull String account) {
        System.out.println(account);

        WebTarget target = CLIENT.target(HAVE_I_BEEN_PAWNED_API).resolveTemplate("account", account);

        String response = target.request(MediaType.APPLICATION_JSON).get(String.class);

        System.out.println(target.getUri().toString());

        if (response != null) {
            System.out.println(response);

            JsonReader reader = Json.createReader(new StringReader(response));
            JsonArray jsonArray = reader.readArray();

            System.out.println(jsonArray.size());
            JsonObject jsonObject = jsonArray.getJsonObject(0);

            System.out.println(jsonObject.toString());

            CLIENT.close();
            return Response.ok().build();

        }

        CLIENT.close();
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
