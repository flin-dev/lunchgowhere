package app.lunchgowhere.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

@Data
@Builder
public class    LocationSubmission implements Message<LocationSubmission> {

    private String reason;
    private String storeName;
    private String sender;

    @Override
    public LocationSubmission getPayload() {
        return this;
    }

    @Override
    public MessageHeaders getHeaders() {
        return null;
    }

}
