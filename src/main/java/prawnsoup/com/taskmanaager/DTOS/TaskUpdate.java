package prawnsoup.com.taskmanaager.DTOS;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
//DTO responsible for updating tasks through json object passed through request
public class TaskUpdate {
    private String taskname;
    private String taskdetails;
    private Boolean finish;

    private String taskdate;

    private Boolean delete;
}
