package app.lunchgowhere.dto.request;


import lombok.Builder;
import lombok.Data;
import org.springframework.web.util.HtmlUtils;

@Data
@Builder
public class LocationSubmission  {

    private String reason;
    private String storeName;
    private String sender;
    private boolean isEscaped = false;

    public LocationSubmission getEscapedObject() {
           if(!isEscaped) {
               htmlEscape();
               isEscaped = true;
           }
           return this;
    }

    public void htmlEscape() {
        this.reason = HtmlUtils.htmlEscape(this.reason);
        this.storeName = HtmlUtils.htmlEscape(this.storeName);
        this.sender = HtmlUtils.htmlEscape(this.sender);
    }
}
