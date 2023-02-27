package prawnsoup.com.taskmanaager.services;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
@NoArgsConstructor
public class TaskService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String timezone = "GMT+0";

    public Date getDate(String taskdate) throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone(this.timezone));
        return dateFormat.parse(taskdate);
    }
}
