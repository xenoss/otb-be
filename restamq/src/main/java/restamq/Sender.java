package restamq;

import api.Message;

public interface Sender {
    void send(Message message);
}
