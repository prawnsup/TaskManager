package prawnsoup.com.taskmanaager.DTOS;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

//used to get tasks through either taskdate or taskname
public class GetTask {
    String taskname;

    String taskdate;
    Boolean status;
}
