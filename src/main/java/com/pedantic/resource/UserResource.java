package com.pedantic.resource;

import com.pedantic.entities.User;
import com.pedantic.services.MySessionStore;
import com.pedantic.services.PersistenceService;
import com.pedantic.services.QueryService;
import com.pedantic.services.SecurityUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private PersistenceService persistenceService;
    @Inject
    private QueryService queryService;
    @Inject
    private Logger logger;
    @Context
    private UriInfo uriInfo;
    @Inject
    private MySessionStore sessionStore;
    @Inject
    private SecurityUtil securityUtil;

    @POST
    public Response createUser(@Valid User user) {
        persistenceService.saveUser(user);

        //Another way to build the path to a given resource. No string concatenation
        return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getId().toString()).build()).build();
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@FormParam("email") String email, @FormParam("password") String password) {
        try {
            logger.log(Level.INFO, "email/password", email + "/" + password);
            authenticateUser(email, password);
            String token = getToken(email, password);
            sessionStore.setEmail(email);
            sessionStore.setEncryptedPassword(securityUtil.encodeText(password));
            return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();

        } catch (Exception e) {
            return Response.status(UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") Long id) {
        User user = queryService.findUserById(id);

        if (user == null) {
            return Response.status(NOT_FOUND).build();
        }

        return Response.ok(user).build();
    }

    private String getToken(String email, String password) {
        Key key = securityUtil.generateKey(securityUtil.encodeText(password));
        String token = Jwts.builder().setSubject(email).setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date()).setExpiration(toDate(LocalDateTime.now().plusMinutes(15)))
                .signWith(SignatureAlgorithm.HS512, key).setAudience(uriInfo.getBaseUri().toString())
                .compact();

        logger.log(Level.INFO, "Generated token is {0}", token);
        return token;
    }

    private void authenticateUser(String email, String password) throws Exception {
        User user = queryService.findUserByCredentials(email, password);
        if (user == null) {
            throw new SecurityException("Email or password incorrect");
        }
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
