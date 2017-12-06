package test;

import api.Message;
import org.jboss.logging.Logger;

import javax.ws.rs.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/messages")
@Consumes({"application/json"})
@Produces({"application/json"})
public class Messages {
    private final static Logger log = Logger.getLogger(Messages.class.getName());

    @GET
    public List<Message> get(@DefaultValue("10") @QueryParam("count") int count) {
        log.info("get count=" + count + "...");

        return MessageGenerator.generate()
                .limit(count)
                .peek(m -> log.debug("message = " + m))
                .collect(toList());
    }
}
