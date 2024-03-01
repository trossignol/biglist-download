package fr.rossi.biglistdownload;

import java.time.Duration;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api")
public class Resource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @GET
    @Path("/stream")
    @Produces("text/event-stream")
    public Multi<String> stream() {
        return Multi.createFrom().items(1, 2, 3, 4, 5)
                .onItem().call(i -> Uni.createFrom().nullItem().onItem().delayIt().by(Duration.ofMillis(500)))
                .map(tick -> tick.toString());
    }
}