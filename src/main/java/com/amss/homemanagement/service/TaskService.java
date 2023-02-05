package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.exception.model.ForbiddenException;
import com.amss.homemanagement.model.*;
import com.amss.homemanagement.repository.TaskRepository;
import jakarta.transaction.Transactional;
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
    private final NotificationService notificationService;

    @Transactional
    public Task create(Task task, UUID familyId, UUID assigneeId) {
        User creator = userService.getById(securityService.getUserId());
        Family family = familyService.getById(familyId);
        if (familyService.getFamilyMember(creator, family).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN); //TODO: Change message
        }

        task.setCreator(creator);
        task.setFamily(family);
        task.setCreationDate(LocalDateTime.now());
        task.setState(TO_DO);

        if (assigneeId != null) {
            User assignee = userService.getById(assigneeId);
            task.setAssignee(assignee);
            Task persistedTask = taskRepository.save(task);
            notificationService.create(assignee, persistedTask);
            return persistedTask;
        }
        return taskRepository.save(task);
    }

    public Task getById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "task", id));
        if (familyService.getFamilyMember(user, task.getFamily()).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN); // TODO: Change message
        }
        return task;
    }

    @Transactional
    public Task updateById(UUID id, Task task) {
        Task existingTask = getById(id);
        existingTask.setState(task.getState());
        existingTask.setDescription(task.getDescription());
        existingTask.setPriority(task.getPriority());
        existingTask.setTitle(task.getTitle());

        if(!existingTask.getAssignee().getId().equals(task.getAssignee().getId())) {
            existingTask.setAssignee(userService.getById(task.getAssignee().getId()));
            Task persistedTask = taskRepository.save(existingTask);
            notificationService.create(persistedTask.getAssignee(), persistedTask);
            return persistedTask;
        }
        return taskRepository.save(existingTask);
    }

    public List<Task> getAllByFamilyId(UUID familyId) {
        return taskRepository.findByFamily_Id(familyId);
    }

    public void deleteById(UUID id) {
        taskRepository.delete(getById(id));
    }

    public List<Notification> getNotificationsByTaskId(UUID taskId) {
        User user = userService.getById(securityService.getUserId());
        Task task = getById(taskId);
        if (familyService.getFamilyMember(user, task.getFamily()).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN); // TODO: Change message
        }
        return notificationService.getByTaskId(taskId);
    }
}
