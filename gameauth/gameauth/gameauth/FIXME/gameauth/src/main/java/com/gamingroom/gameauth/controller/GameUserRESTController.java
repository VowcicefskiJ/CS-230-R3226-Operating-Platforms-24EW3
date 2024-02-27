package com.gamingroom.gameauth.controller;

import io.dropwizard.auth.Auth;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.gamingroom.gameauth.auth.GameUser;
import com.gamingroom.gameauth.dao.GameUserDB;
import com.gamingroom.gameauth.representations.GameUserInfo;

/**
 * Controller class to manage game user operations via REST API.
 */
@Path("/gameusers")
@Produces(MediaType.APPLICATION_JSON) // Ensure that all responses are in JSON format
public class GameUserRESTController {

    private final Validator validator;

    public GameUserRESTController(Validator validator) {
        this.validator = validator;
    }

    /**
     * Get all game users.
     * @param user Authenticated user
     * @return Response with list of all game users
     */
    @PermitAll
    @GET
    public Response getGameUsers(@Auth GameUser user) {
        return Response.ok(GameUserDB.getGameUsers()).build();
    }

    /**
     * Get a single game user by ID.
     * @param id User ID
     * @param user Authenticated user
     * @return Response with game user details
     */
    @RolesAllowed("USER")
    @GET
    @Path("/{id}")
    public Response getGameUserById(@PathParam("id") Integer id, @Auth GameUser user) {
        GameUserInfo gameUserInfo = GameUserDB.getGameUser(id);
        if (gameUserInfo != null) {
            return Response.ok(gameUserInfo).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    /**
     * Create a new game user.
     * @param gameUserInfo Game user information
     * @param user Authenticated user
     * @return URI of the created game user
     * @throws URISyntaxException If URI syntax is incorrect
     */
    @RolesAllowed("ADMIN")
    @POST
    public Response createGameUser(GameUserInfo gameUserInfo, @Auth GameUser user) throws URISyntaxException {
        Set<ConstraintViolation<GameUserInfo>> violations = validator.validate(gameUserInfo);
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<>();
            for (ConstraintViolation<GameUserInfo> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        GameUserDB.updateGameUser(gameUserInfo.getId(), gameUserInfo);
        return Response.created(new URI("/gameusers/" + gameUserInfo.getId())).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateGameUserById(@PathParam("id") Integer id, GameUserInfo gameUserInfo) {
        Set<ConstraintViolation<GameUserInfo>> violations = validator.validate(gameUserInfo);
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<>();
            for (ConstraintViolation<GameUserInfo> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        GameUserInfo existingUserInfo = GameUserDB.getGameUser(id);
        if (existingUserInfo != null) {
            gameUserInfo.setId(id);
            GameUserDB.updateGameUser(id, gameUserInfo);
            return Response.ok(gameUserInfo).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response removeGameUserById(@PathParam("id") Integer id) {
        GameUserInfo gameUserInfo = GameUserDB.getGameUser(id);
        if (gameUserInfo != null) {
            GameUserDB.removeGameUser(id);
            return Response.ok().build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
