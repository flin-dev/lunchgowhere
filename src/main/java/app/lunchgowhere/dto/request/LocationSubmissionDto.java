package app.lunchgowhere.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.util.HtmlUtils;

@Data
@Builder
public class LocationSubmissionDto {

    private Long roomId;

    private String reason;

    private String description;

    private String name;

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
        this.description = HtmlUtils.htmlEscape(this.description);
        this.reason = HtmlUtils.htmlEscape(this.reason);
        this.name = HtmlUtils.htmlEscape(this.name);
        this.sender = HtmlUtils.htmlEscape(this.sender);
    }
}
