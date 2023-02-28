package prawnsoup.com.taskmanaager.DTOS;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
//TaskDTO responsible for processing rest controller request and returning JSON responses
public class TaskDTO {
    @JsonIgnore
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @JsonIgnore
    private static final String timezone = "GMT+0";

    @NonNull
    private String taskname;

    @NonNull
    private String taskdetails;

    @NonNull

    private String taskdate;
    @NonNull
    private Boolean status=false; // will always be set to false
    public Date getDate(String timezone) throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return dateFormat.parse(this.taskdate);
    }
    public void setDate(Date date) {
        dateFormat.setTimeZone(TimeZone.getTimeZone(this.timezone));
        this.taskdate = dateFormat.format(date);
    }

}
