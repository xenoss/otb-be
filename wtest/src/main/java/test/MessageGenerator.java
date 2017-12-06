package test;

import api.Message;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MessageGenerator {
    public static Stream<Message> generate() {
        return IntStream.iterate(0, i -> i + 1).mapToObj(c -> Message
                .builder()
                .receiver("receiver-" + RandomStringUtils.randomAlphabetic(10) + "-" + c)
                .sender("sender-" + RandomStringUtils.randomAlphabetic(10) + "-" + c)
                .text("text-" + RandomStringUtils.randomAlphabetic(10) + "-" + c)
                .timestamp(System.currentTimeMillis())
                .build());
    }
}
