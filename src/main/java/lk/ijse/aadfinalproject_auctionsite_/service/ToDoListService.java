package lk.ijse.aadfinalproject_auctionsite_.service;

import lk.ijse.aadfinalproject_auctionsite_.dto.TodoDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.Todo;

import java.time.LocalDate;

public interface ToDoListService {
    void addTask(TodoDTO taskDTO);

    Iterable<Todo> getAllTasks();

    Iterable<Todo> getTasksByDate(LocalDate selectedDate);

    boolean markTaskAsCompleted(String taskId);
}
