package com.pedantic.resource;

import com.pedantic.services.PersistenceService;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("payroll")
public class PayrollResource {

    @Inject
    private Logger logger;
    @Inject
    private PersistenceService persistenceService;
    @Resource
    private ManagedExecutorService managedExecutorService;

    @Path("process")
    @POST
    public void processPayroll(@Suspended AsyncResponse asyncResponse) {
        String initialThread = Thread.currentThread().getName();
        logger.log(Level.INFO, "Thread: " + initialThread + " in action...");
        //Java 8 Lambda. Empty Lambda expression denotes a thread
        managedExecutorService.execute(() -> {

            try {
                String processingThread = Thread.currentThread().getName();
                logger.log(Level.INFO, "Processing thread: " + processingThread);
                //Simulate payroll computation that could take time. Think of 5000 employees
                Thread.sleep(5000);
                String respBody = "Process initated in " + initialThread + " and finished in " + processingThread;
                asyncResponse.resume(Response.ok(respBody).build());
            } catch (InterruptedException e) {
               logger.log(Level.SEVERE, null, e);
            }
        });

    }
}
