package com.pedantic.config;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.ejb.Stateless;


public class CDIConfig {

    @Produces
    @Dependent
    @PersistenceContext
    public EntityManager entityManager;


    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
