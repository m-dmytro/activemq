package cm.hw.activemq.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class MessageObj {

    private final String id;
    private final String message;

    @JsonCreator
    public MessageObj(@JsonProperty("messageId") String id,
                      @JsonProperty("message") String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageObj that = (MessageObj) o;
        return Objects.equals(id, that.id) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message);
    }

    @Override
    public String toString() {
        return "MessageObj{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}
