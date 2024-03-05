package fr.rossi.biglistdownload;

import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/stream")
@RegisterRestClient(configKey = "internal-api")
public interface Client {

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    Multi<String> stream();
}
