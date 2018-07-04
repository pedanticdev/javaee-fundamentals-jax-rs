package com.pedantic.resource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("sse")
public class ServerSentEventResource {

    @Context
    private Sse sse;
    @Inject
    private Logger logger;
    private SseBroadcaster sseBroadcaster;
    private SseEventSink eventSink;

    @PostConstruct
    private void init() {
        sseBroadcaster = sse.newBroadcaster();
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void fetch(@Context SseEventSink sseEventSink) {
        sseBroadcaster.register(sseEventSink);
        this.eventSink = sseEventSink;

        logger.log(Level.INFO,"SSE opened!" );
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response broadcast(@FormParam("message") String message) {
        OutboundSseEvent broadcastEvent = sse.newEvent(message);
        sseBroadcaster.broadcast(broadcastEvent);
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response broadcastEmployee(String employee) {
        OutboundSseEvent broadcastEvent = sse.newEventBuilder().name("employee").data(employee).
                mediaType(MediaType.TEXT_PLAIN_TYPE).build();

        sseBroadcaster.broadcast(broadcastEvent);
        return Response.ok().status(Response.Status.OK).build();
    }


    @PreDestroy
    private void destroy() {
        if (eventSink != null) {
            eventSink.close();

        }
    }
}
