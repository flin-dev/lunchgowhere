package app.lunchgowhere.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.util.HtmlUtils;

@Data
@Builder
public class LocationSubmissionDto {

    private String reason;

    private String storeName;

    private String sender;


    private boolean isEscaped = false;

    @JsonIgnore
    public LocationSubmissionDto getEscapedObject() {
           if(!isEscaped) {
               htmlEscape();
               isEscaped = true;
           }
           return this;
    }

    @JsonIgnore
    public void htmlEscape() {
        this.reason = HtmlUtils.htmlEscape(this.reason);
        this.storeName = HtmlUtils.htmlEscape(this.storeName);
        this.sender = HtmlUtils.htmlEscape(this.sender);
    }
}
