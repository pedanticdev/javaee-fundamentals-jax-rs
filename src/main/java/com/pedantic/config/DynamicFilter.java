package com.pedantic.config;

import javax.inject.Inject;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class DynamicFilter implements DynamicFeature {
    @Inject
    private Logger logger;
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
        MaxAge maxAge = resourceInfo.getResourceMethod().getAnnotation(MaxAge.class);
        if (maxAge == null) {
            return;
        }
        logger.log(Level.INFO, resourceInfo.getResourceMethod().getName());
        DynamicServerResponseFilter responseFilter = new DynamicServerResponseFilter(maxAge.value());
        featureContext.register(responseFilter);
    }
}
