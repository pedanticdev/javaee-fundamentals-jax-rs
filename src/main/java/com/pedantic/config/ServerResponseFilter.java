package com.pedantic.config;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class ServerResponseFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext reqCtx, ContainerResponseContext resCtx) throws IOException {
        if (reqCtx.getMethod().equals("GET")) {
            CacheControl cacheControl = new CacheControl();
            cacheControl.setMaxAge(150);
            resCtx.getHeaders().add("Cache-Control", cacheControl);
        }
    }
}
