package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.exception.model.ConflictException;
import com.amss.homemanagement.exception.model.ForbiddenException;
import com.amss.homemanagement.model.Family;
import com.amss.homemanagement.model.FamilyMember;
import com.amss.homemanagement.model.Task;
import com.amss.homemanagement.model.User;
import com.amss.homemanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.amss.homemanagement.model.State.TO_DO;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final SecurityService securityService;
    private final FamilyService familyService;


    public Task create(Task task, UUID familyId) {
        User user = userService.getById(securityService.getUserId());
        Family family = familyService.getById(familyId);
        if (getFamilyMember(user, family).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN); //TODO: Change message
        }

        task.setUser(user);
        task.setFamily(family);
        task.setDate(LocalDateTime.now());
        task.setState(TO_DO);
        return taskRepository.save(task);
    }

    public Task getById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "task", id));
        if (getFamilyMember(user, task.getFamily()).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN); // TODO: Change message
        }
        return task;
    }

    public Task updateById(UUID id, Task task) {
        Task existingTask = getById(id);
        existingTask.setState(task.getState());
        existingTask.setDescription(task.getDescription());
        existingTask.setPriority(task.getPriority());
        existingTask.setTitle(task.getTitle());
        return taskRepository.save(existingTask);
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public void deleteById(UUID id) {
        Task task = getById(id);
        taskRepository.delete(task);
    }

    public Optional<FamilyMember> getFamilyMember(User user, Family family) {
        return user.getFamilyMembers().stream()
                .filter(familyMember -> familyMember.getFamily().equals(family))
                .findFirst();
    }
}
