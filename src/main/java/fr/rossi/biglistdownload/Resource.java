package fr.rossi.biglistdownload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.rossi.biglistdownload.dataload.Dataload;
import fr.rossi.biglistdownload.util.HackIssue26253;
import fr.rossi.biglistdownload.util.JsonMapper;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.stream.Collectors;

@Path("/api")
public class Resource {

    @Inject
    JsonMapper jsonMapper;

    @RestClient
    Client localClient;

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Long count() {
        return Person.count();
    }

    @GET
    @Path("/stream")
    @Produces("text/event-stream")
    @Transactional
    @TransactionConfiguration(timeout = 2)
    public Multi<String> stream() {
        System.out.println("<<<<<< Start new stream >>>>>>>");
        return new HackIssue26253(Multi.createFrom()
                .items(Person.streamAll(Sort.by("firstname"))).map(this.jsonMapper::toJson))
                .producesSSE()
                //.onItem().call(i -> Uni.createFrom().nullItem().onItem().delayIt().by(Duration.ofMillis(10)))
                ;
    }

    @GET
    @Path("/client/test")
    public Uni<Long> testStream() {
        return new HackIssue26253(this.localClient.stream())
                .consumesSSE()
                .map(json -> this.jsonMapper.fromJson(json, Person.class))
                .map(person -> person.firstname)
                .log()
                .collect().with(Collectors.counting());
    }
}