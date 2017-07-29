package com.pedantic.config;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class DynamicFilter implements DynamicFeature {
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
        MaxAge maxAge = resourceInfo.getResourceMethod().getAnnotation(MaxAge.class);
        if (maxAge == null) {
            return;
        }
        DynamicServerResponseFilter responseFilter = new DynamicServerResponseFilter(maxAge.value());
        featureContext.register(responseFilter);
    }
}
