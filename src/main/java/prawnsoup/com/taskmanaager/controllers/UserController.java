package prawnsoup.com.taskmanaager.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import prawnsoup.com.taskmanaager.DTOS.GetTask;
import prawnsoup.com.taskmanaager.DTOS.TaskDTO;
import prawnsoup.com.taskmanaager.DTOS.TaskUpdate;
import prawnsoup.com.taskmanaager.models.Task;
import prawnsoup.com.taskmanaager.models.User;
import prawnsoup.com.taskmanaager.repositories.TaskRepository;
import prawnsoup.com.taskmanaager.repositories.UserRepository;
import prawnsoup.com.taskmanaager.services.TaskService;
import prawnsoup.com.taskmanaager.services.UserDetailsServiceImpl;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    /**
     * Add a new task for a user
     * @param taskDTO  DTO object with [String taskname ,String taskdetails, String taskdate , Boolean status]
     * @return ResponseEntity with status code and message
     */

    @PostMapping("/addtask")
    public ResponseEntity<?> addTask(@RequestBody TaskDTO taskDTO){
        //loading with taskkdto
        //convert from taskdto to task object using object mapper.
        //set task user to be user from user principal , save task to repository
       // return updated task dto
       //load current user


       try {
           User user;
           Task task= ConvertToTask(taskDTO);
           user =  userDetailsService.loadUserFromPricipal();
           if(taskRepository.findByTaskname(task.getTaskname()).isPresent()){
               return ResponseEntity.badRequest().body("Taskname already exists choose a different one");
           }
           task.setUser(user);
           taskRepository.save(task);
           taskDTO=convertDTO(task);
           return ResponseEntity.ok(taskDTO);
       }
       catch (Exception e){
            return ResponseEntity.badRequest().body("Task not defined correctly");
       }
    }

    /**
     * Get all tasks for a user with specified status and date (optional) or taskname. Get username from principal
     * @param getTask DTO object with [String taskname , String taskdate , Boolean status]
     * @return List of tasks matching the search criteria
     */
    @GetMapping("/tasks")
    public ResponseEntity<?> getTasks(@RequestBody GetTask getTask) {
        // retrieve user from principal
        User user;
        List<Task> tasks;
        user = (User) userDetailsService.loadUserFromPricipal();
        try {
            if (taskRepository.findAllByUserOrderByTaskIDAsc(user).isEmpty()) {
                return ResponseEntity.badRequest().body("User doesnt have any tasks");
            }

            // retrieve tasks based on taskname
            if(getTask.getTaskname() != null && !getTask.getTaskname().describeConstable().isEmpty()){
                if(taskRepository.findByTaskname(getTask.getTaskname()).isPresent()){
                    Task task= taskRepository.findByTaskname(getTask.getTaskname()).get();
                    TaskDTO taskDTO= convertDTO(task);
                    return ResponseEntity.ok().body(taskDTO);
                }
            }

            if ((getTask.getTaskdate() == null || getTask.getTaskdate().describeConstable().isEmpty())
                    && (getTask.getStatus() == null || getTask.getStatus().describeConstable().isEmpty())) {
                tasks = taskRepository.findAllByUserOrderByTaskIDAsc(user).get();
            } else if(getTask.getStatus() == null || getTask.getStatus().describeConstable().isEmpty()
                    && getTask.getTaskdate() != null && getTask.getTaskdate().describeConstable().isPresent()) {
                tasks = taskRepository.findAllByTaskdateAndUserOrderByTaskIDAsc(taskService.getDate(getTask.getTaskdate()), user).get();
            } else if (getTask.getStatus() != null && getTask.getStatus().describeConstable().isPresent()
                    && (getTask.getTaskdate() == null || getTask.getTaskdate().describeConstable().isEmpty())) {
                if(getTask.getStatus()){
                    tasks= taskRepository.findAllByUserAndFinishTrueOrderByTaskIDAsc(user).get();
                }
                else {
                    tasks = taskRepository.findAllByUserAndFinishFalseOrderByTaskIDAsc(user).get();
                }
            } else {//if (getTask.getTaskdate().describeConstable().isPresent() && getTask.getStatus().describeConstable().isPresent()){
                if (getTask.getStatus() != null && getTask.getStatus()) {
                    tasks = taskRepository.findAllByUserAndFinishTrueAndTaskdateOrderByTaskIDAsc(user, taskService.getDate(getTask.getTaskdate())).get();
                } else {
                    tasks = taskRepository.findAllByUserAndFinishFalseAndTaskdateOrderByTaskIDAsc(user, taskService.getDate(getTask.getTaskdate())).get();
                }
            }
            List<TaskDTO> taskDTOS= getTaskDTOs(tasks);
            return ResponseEntity.ok().body(taskDTOS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Update details and/or status of a task (optional), or delete task (optional)
     * @Param TaskUpdate DTO containing[ String Taskname(if empty then throw an error and return badrequest response), TaskDetails, Boolean finish , Boolean task]
     * @return ResponseEntity with status code and message
     */
    @PutMapping("/updatetasks")
    public ResponseEntity<?> updateTasks(@RequestBody TaskUpdate taskUpdate) {
        try {
            // Retrieve the current user from principal
            User user = (User) userDetailsService.loadUserFromPricipal();

            // Retrieve the task from taskname
            Optional<Task> optionalTask = taskRepository.findByTaskname(taskUpdate.getTaskname());
            if (!optionalTask.isPresent()) {
                return ResponseEntity.badRequest().body("Task not found.");
            }
            Task task = optionalTask.get();

            // Check if the task belongs to the current user
            if (!task.getUser().equals(user)) {
                return ResponseEntity.badRequest().body("You do not have permission to update this task.");
            }

            // Update details of task (optional)
            if (taskUpdate.getTaskdetails() != null && taskUpdate.getTaskdetails().describeConstable().isPresent()) {
                task.setTaskdetails(taskUpdate.getTaskdetails());
            }

            // Update status of task (optional)
            if (taskUpdate.getFinish() != null) {
                task.setFinish(taskUpdate.getFinish());
            }

            // Delete task (optional)
            if (taskUpdate.getDelete() != null && taskUpdate.getDelete().describeConstable().isPresent() && taskUpdate.getDelete()) {
                taskRepository.delete(task);
                return ResponseEntity.ok().body("Task deleted successfully.");
            }

            // TaskDate optional
            if (taskUpdate.getTaskdate() != null && taskUpdate.getTaskdate().describeConstable().isPresent()) {
                task.setTaskdate(taskService.getDate(taskUpdate.getTaskdate()));
            }

            // Save the updated task to the database
            taskRepository.save(task);
            // Convert task to DTO and return it in the response body
            TaskDTO taskDTO = convertDTO(task);
            return ResponseEntity.ok().body(taskDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating task.");
        }
    }

   // @RequestMapping("/test")
   // public ResponseEntity<?> test(){
     //   return ResponseEntity.ok().build();
   // }

    /**
     *
     * @param tasks
     * @return List<TaskDTOs>
     */
    private List<TaskDTO> getTaskDTOs(List<Task> tasks){
        return tasks.stream()
                .map(this::convertDTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param task
     * @return TaskDTO object
     */

    private TaskDTO convertDTO(Task task) {
        TaskDTO taskDTO = objectMapper.convertValue(task, TaskDTO.class);
        taskDTO.setDate(task.getTaskdate());
        return taskDTO;
    }

    /**
     *
     * @param taskDTO
     * @return Task object generated from task.
     * @throws ParseException
     */
    private Task ConvertToTask(TaskDTO taskDTO) throws ParseException {
        Date date= taskDTO.getDate("GMT+0");
        taskDTO.setDate(date);
        Task task = objectMapper.convertValue(taskDTO, Task.class);
        return task;
    }



}




