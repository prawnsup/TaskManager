package prawnsoup.com.taskmanaager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name= "Task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer taskID;
    @Column(name="taskdetails")
    private String taskdetails;
    @Column(name= "taskdate")
    private Date taskdate;

    @Column(name = "taskname")
    private String taskname;

    private Boolean finish; // true if completed false is unfinished

    @ManyToOne
    @JsonIgnore
    private User user; // will automatically create foreign keys for me


}
