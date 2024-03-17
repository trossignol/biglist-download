package fr.rossi.biglistdownload;

import fr.rossi.biglistdownload.util.HackIssue26253;
import fr.rossi.biglistdownload.util.JsonMapper;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import io.quarkus.panache.common.Sort;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/api")
public class Resource {

    private static final Logger LOG = Logger.getLogger(Resource.class);

    @Inject
    JsonMapper jsonMapper;

    @RestClient
    Client localClient;

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Long count() {
        LOG.info(">>> Count >>>");
        return Person.count();
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> listAll(@QueryParam("limit") @Nullable Integer limit) {
        return limit == null
                ? Person.listAll()
                : Person.list("FROM Person ORDER BY firstname LIMIT :limit", Map.of("limit", limit));
    }

    @GET
    @Path("/stream")
    @Produces("text/event-stream")
    @TransactionConfiguration(timeout = 200)
    @Blocking // I don't understand why I need to put this @Blocking
    public Multi<String> stream() {
        LOG.info(">>> Start new stream >>>");
        return new HackIssue26253(Multi.createFrom()
                .items(Person.streamAll(Sort.by("firstname"))).map(this.jsonMapper::toJson))
                .producesSSE()
                //.onItem().call(i -> Uni.createFrom().nullItem().onItem().delayIt().by(Duration.ofMillis(10)))
                ;
    }

    @GET
    @Path("/client")
    public Uni<Long> testStream() {
        LOG.info(">>> Test stream >>>");
        return new HackIssue26253(this.localClient.stream())
                .consumesSSE()
                .map(json -> this.jsonMapper.fromJson(json, Person.class))
                .map(person -> person.firstname)
                .log()
                .collect().with(Collectors.counting());
    }
}