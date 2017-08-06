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
    private PawnedDomains pawnedDomains;
    @Inject
    private Logger logger;
    private final Client CLIENT = ClientBuilder.newClient();
    private static final String HAVE_I_BEEN_PAWNED_API = "https://haveibeenpwned.com/api/v2/breachedaccount/{account}";

    @GET
    @Path("pawned")
    public Response haveIBeenPawned(@QueryParam("account") @NotNull String account) {

        WebTarget target = CLIENT.target(HAVE_I_BEEN_PAWNED_API).resolveTemplate("account", account);

        String response = target.request(MediaType.APPLICATION_JSON).get(String.class);

        if (response != null) {
            logger.log(Level.INFO, response);

            JsonReader reader = Json.createReader(new StringReader(response));
            JsonArray jsonArray = reader.readArray();

            logger.log(Level.INFO, "Returned json array has a size of {0}", jsonArray.size());
            for (int i = 0; i < jsonArray.size(); i++) {

                String domain = jsonArray.getJsonObject(i).getString("Domain");
                System.out.println(domain);

                pawnedDomains.getPawnedDomains().add(domain);


                JsonObject jsonObject = jsonArray.getJsonObject(i);

                logger.log(Level.INFO, jsonObject.toString());
            }


            CLIENT.close();
            return Response.ok(pawnedDomains).build();

        }

        CLIENT.close();
        logger.log(Level.SEVERE, "No response returned");

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
