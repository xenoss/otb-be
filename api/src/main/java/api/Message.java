package api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class Message {
    private String sender;

    private String receiver;

    private String text;

    private long timestamp;

    public Message() {
    }

    public Message(String sender, String receiver, String text, long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.timestamp = timestamp;
    }

    public static Message fromString(String txt) {
        return Binder.unmarshal(txt, Message.class);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String toJson() {
        return Binder.marshal(this);
    }

    @JsonIgnore
    public String getId() {
        return sender + "-" + receiver + "-" + timestamp;
    }
}
