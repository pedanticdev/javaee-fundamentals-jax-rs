package com.pedantic.resource;

import com.pedantic.entities.Employee;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

@RequestScoped
public class JaxrsClient {
    private Client client;
    WebTarget webTarget;

    private final String haveIBeenPawned = "https://haveibeenpwned.com/api/v2/breachedaccount"; //https://haveibeenpwned.com/api/v2/breachedaccount/{account}


    @PostConstruct
    private void init() {
        client = ClientBuilder.newBuilder().connectTimeout(7, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS).build();
//        ClientBuilder.newClient();

        webTarget = client.target(haveIBeenPawned);
    }

    @PreDestroy
    private void destroy() {
        if (client != null) {

            //Be sure to close the programmatic to prevent resource leakage

            client.close();
        }

    }

    public int checkBreaches(String email) {

        JsonArray jsonValues = webTarget.path("{account}")
                .resolveTemplate("account", email).request(MediaType.TEXT_PLAIN).get(JsonArray.class);


        parseJsonArray(jsonValues);


        return jsonValues.size();
    }

    public JsonArray getBreaches(String email) {
        return webTarget.path("{account}")
                .resolveTemplate("account", email).request(MediaType.TEXT_PLAIN).get(JsonArray.class);
    }

    public void checkBreachesRx(String email) {


        CompletionStage<Response> responseCompletionStage = webTarget.path("{account}")
                .resolveTemplate("account", email).request().rx().get();

        responseCompletionStage.thenApply(response -> response.readEntity(JsonArray.class))
                .thenAccept(this::parseJsonArray);
    }

    private void parseJsonArray(JsonArray jsonArray) {

        for (JsonValue jsonValue : jsonArray) {

            JsonObject jsonObject = jsonValue.asJsonObject();

            String domain = jsonObject.getString("Domain");
            String breachDate = jsonObject.getString("BreachDate");

            System.out.println("Breach name is " + domain);
            System.out.println("Breach date is " + breachDate);


            System.out.println();

        }
        System.out.println("Breach size is " + jsonArray.size());
    }

    public void postEmployeeToSSE(Employee employee) {
        String json = JsonbBuilder.create().toJson(employee);

        int status = client.target("http://localhost:8080/javaee-fundamentals-jax-rs/api/v1/sse").request(MediaType.TEXT_PLAIN)

                .post(Entity.text(json)).getStatus();

        System.out.println("Status received " + status);
        System.out.println(json);


    }


}
