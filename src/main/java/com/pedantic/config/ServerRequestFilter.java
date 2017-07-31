package com.pedantic.config;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//@Provider
//@PreMatching
//@Priority(Priorities.AUTHENTICATION)
public class ServerRequestFilter implements ContainerRequestFilter {

    @Inject
    private Logger logger;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        //Assuming say a firewall or security that does not allow any of the HTTP methods, this
        //can be used to circumvent that.
        String methodOverride = ctx.getHeaderString("X-Http-Method-Override");
        if (methodOverride != null) {
            ctx.setMethod(methodOverride);
        }
        logger.log(Level.INFO, ctx.getMethod());
        logger.log(Level.INFO, ctx.getUriInfo().getAbsolutePath().toString());
    }
}
