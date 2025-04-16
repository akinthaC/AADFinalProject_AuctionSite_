package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.dto.TodoDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.Todo;
import lk.ijse.aadfinalproject_auctionsite_.repo.ToDoListRepo;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.ToDoListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ToDo")
@CrossOrigin(origins = "*")
public class ToDoListController {
    @Autowired
    private ToDoListServiceImpl toDoListService;

    @PostMapping
    public ResponseEntity<String> addTask(@RequestBody TodoDTO taskDTO) {
        try {
            toDoListService.addTask(taskDTO);
            return new ResponseEntity<>("Task added successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add task.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Optional: Endpoint for getting tasks
    @GetMapping
    public ResponseEntity<?> getTasksByDate(@RequestParam(value = "date", required = false) String date) {
        System.out.println(date);
        try {
            LocalDate selectedDate = date != null ? LocalDate.parse(date) : null;
            System.out.println(selectedDate);
            Iterable<Todo> tasks = toDoListService.getTasksByDate(selectedDate);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching tasks.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<String> updateTaskStatus(@PathVariable String taskId) {
        boolean updated = toDoListService.markTaskAsCompleted(taskId);
        if (updated) {
            return ResponseEntity.ok("Task marked as completed");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
    }
}
