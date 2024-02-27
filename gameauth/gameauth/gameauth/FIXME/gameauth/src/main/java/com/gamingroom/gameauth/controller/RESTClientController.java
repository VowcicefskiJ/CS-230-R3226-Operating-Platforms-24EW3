package com.gamingroom.gameauth.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class is a REST client controller that demonstrates how to interact with
 * the GameUserRESTController's endpoints. It uses JAX-RS Client API to make HTTP requests.
 */
@Produces(MediaType.TEXT_PLAIN)
@Path("/client/")
public class RESTClientController {

    private Client client;

    /**
     * Constructs a RESTClientController with a given JAX-RS client.
     * @param client The JAX-RS client used to make HTTP requests.
     */
    public RESTClientController(Client client) {
        this.client = client;
    }

    /**
     * Retrieves all game users from the '/gameusers' endpoint.
     * @return A string representation of all game users in JSON format.
     */
    @GET
    @Path("/gameusers")
    public String getGameUsers() {
        // Target the endpoint for fetching all game users.
        WebTarget webTarget = client.target("http://localhost:8080/gameusers");
        // Build the request, specifying that the response should be in JSON format.
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        // Execute the GET request and receive the response.
        Response response = invocationBuilder.get();
        // Extract the response entity as a string.
        String gameUsersJson = response.readEntity(String.class);
        return gameUsersJson;
    }

    /**
     * Retrieves a single game user by ID from the '/gameusers/{id}' endpoint.
     * @param id The ID of the game user to retrieve.
     * @return A string representation of the game user in JSON format.
     */
    @GET
    @Path("/gameusers/{id}")
    public String getGameUserById(@PathParam("id") int id) {
        // Target the endpoint for fetching a specific game user by ID.
        WebTarget webTarget = client.target("http://localhost:8080/gameusers/" + id);
        // Build the request, specifying that the response should be in JSON format.
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        // Execute the GET request and receive the response.
        Response response = invocationBuilder.get();
        // Extract the response entity as a string.
        String gameUserJson = response.readEntity(String.class);
        return gameUserJson;
    }
}
