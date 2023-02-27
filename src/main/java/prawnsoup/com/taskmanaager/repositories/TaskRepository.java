package prawnsoup.com.taskmanaager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import prawnsoup.com.taskmanaager.models.Task;
import prawnsoup.com.taskmanaager.models.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task , Long> {

   // Optional<List<Task>> findAllByUserOrderByTaskIDAsc(User user);
    Optional<List<Task>> findAllByUserOrderByTaskIDAsc(User user);
 //   Optional<List<Task>> findAllByTasknameAndFinishOrderByTaskIDAsc(String taskname);

    Optional<Task> findByTaskname(String taskname);

    Optional<List<Task>>findAllByTaskdateAndUserOrderByTaskIDAsc(Date taskdate, User user);
    Optional<List<Task>>findAllByUserAndFinishTrueOrderByTaskIDAsc(User user);
    Optional<List<Task>>findAllByUserAndFinishFalseOrderByTaskIDAsc(User user);


    Optional<List<Task>>findAllByUserAndFinishTrueAndTaskdateOrderByTaskIDAsc(User user  , Date taskdate);

   Optional<List<Task>>findAllByUserAndFinishFalseAndTaskdateOrderByTaskIDAsc(User user  , Date taskdate);



}
