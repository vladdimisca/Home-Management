package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.model.Task;
import com.amss.homemanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;


    //public Task create(Task task) { }

    //public Task updateById(UUID id, Task task) { }

    public Task getById(UUID id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "task", id));
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public void deleteById(UUID id) {
        Task task = getById(id);
        taskRepository.delete(task);
    }

}
