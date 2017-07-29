package com.pedantic.config;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import java.io.IOException;

public class DynamicServerResponseFilter implements ContainerResponseFilter {
    private int cacheDuration;

    public DynamicServerResponseFilter(int cacheDuration) {
        this.cacheDuration = cacheDuration;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        if (containerRequestContext.getMethod() == "GET") {
            CacheControl cacheControl = new CacheControl();
            cacheControl.setMaxAge(cacheDuration);
            containerResponseContext.getHeaders().add("Cache-Control", cacheControl);
        }
    }
}
