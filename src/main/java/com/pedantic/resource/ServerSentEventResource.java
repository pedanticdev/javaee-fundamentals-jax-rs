package com.pedantic.resource;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("sse")
public class ServerSentEventResource {

    @Resource
    private ManagedExecutorService managedExecutorService;
    @Inject
    private Logger logger;





    @Path("fetch")
    @GET
    @Produces("text/event-stream")
    public void fetch(@Context Sse sse, @Context SseEventSink eSink) {
        OutboundSseEvent event = sse.newEvent("one-time-event", LocalDateTime.now().toString());
        eSink.send(event);
        System.out.println("event sent");
        eSink.close();
        System.out.println("sink closed");
    }



    @GET
    @Path("domains/{id}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void startDomain(@PathParam("id") final String id, @Context SseEventSink domainSink, @Context Sse sse) {
        managedExecutorService.execute(() -> {
            try {
                domainSink.send(sse.newEventBuilder()
                        .name("domain-progress")
                        .data(String.class, "starting domain " + id + " ...")
                        .build());
                Thread.sleep(5000);
                domainSink.send(sse.newEventBuilder().name("domain-progress").data(String.class, "50%").build());
                Thread.sleep(200);
                domainSink.send(sse.newEventBuilder().name("domain-progress").data(String.class, "60%").build());
                Thread.sleep(2000);
                domainSink.send(sse.newEventBuilder().name("domain-progress").data(String.class, "70%").build());
                Thread.sleep(200);
                domainSink.send(sse.newEventBuilder().name("domain-progress").data(String.class, "99%").build());
                Thread.sleep(1000);
                domainSink.send(sse.newEventBuilder().name("domain-progress").data(String.class, "done").build());
                domainSink.close();
            } catch (InterruptedException i) {
                logger.log(Level.SEVERE, null, i);
            }
        });
    }
}
