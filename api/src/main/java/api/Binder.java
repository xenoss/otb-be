package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class Binder {
    private final static ObjectMapper om = new ObjectMapper();

    @SneakyThrows
    public static String marshal(Object obj) {
        return om.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T unmarshal(String txt, Class<T> clazz) {
        return om.readValue(txt, clazz);
    }
}
