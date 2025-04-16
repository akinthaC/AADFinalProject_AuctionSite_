package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import lk.ijse.aadfinalproject_auctionsite_.dto.TodoDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.Todo;
import lk.ijse.aadfinalproject_auctionsite_.repo.PurchaseRepo;
import lk.ijse.aadfinalproject_auctionsite_.repo.ToDoListRepo;
import lk.ijse.aadfinalproject_auctionsite_.service.ToDoListService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ToDoListServiceImpl implements ToDoListService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ToDoListRepo toDoListRepo;

    @Override
    public void addTask(TodoDTO taskDTO) {
        Todo task = new Todo();
        task.setAssignAdminEmail(taskDTO.getAssignAdminEmail());
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDate(taskDTO.getDate());
        task.setCompleted(taskDTO.getCompleted());
        toDoListRepo.save(task);
    }

    @Override
    public Iterable<Todo> getAllTasks() {
        return toDoListRepo.findAll();
    }

    @Override
    public Iterable<Todo> getTasksByDate(LocalDate selectedDate) {
        if (selectedDate != null) {
            System.out.println(selectedDate);
            return toDoListRepo.findByDate(selectedDate);
        }
        // If no date is selected, return all tasks
        return toDoListRepo.findAll();
    }

    @Override
    public boolean markTaskAsCompleted(String taskId) {
        Optional<Todo> taskOpt = toDoListRepo.findById(Long.valueOf(taskId));

        if (taskOpt.isPresent()) {
            Todo task = taskOpt.get();
            task.setCompleted("Yes"); // Mark as completed
            toDoListRepo.save(task);
            return true;
        }

        return false; // Task not found
    }

}
