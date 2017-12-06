package restamq;

import api.Message;
import lombok.val;
import org.jboss.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import java.util.List;

public class RestClient {
    private final static Logger log = Logger.getLogger(RestClient.class.getName());

    private static final String REST_TARGET_URL = "http://localhost:8080/wtest/test/messages";

    public static final List<Message> get(int count) {
        log.info("get " + REST_TARGET_URL + "?count=" + count + "...");
        val response = ClientBuilder.newClient()
                .target(REST_TARGET_URL)
                .queryParam("count", count)
                .request()
                .get(new GenericType<List<Message>>() {
                });

        log.info("response = " + response);

        return response;
    }
}
