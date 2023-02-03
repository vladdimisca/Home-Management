package com.amss.homemanagement.controller;


import com.amss.homemanagement.dto.TaskDto;
import com.amss.homemanagement.mapper.TaskMapper;
import com.amss.homemanagement.model.Task;
import com.amss.homemanagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    public ResponseEntity<TaskDto> create(@Valid @RequestBody TaskDto taskDto) {
        Task task = taskService.create(taskMapper.mapToEntity(taskDto), taskDto.familyId());
        return ResponseEntity
                .created(URI.create("/api/families/{familyId}/tasks" + task.getId()))
                .body(taskMapper.mapToDto(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> update(@PathVariable("id") UUID id, @Valid @RequestBody TaskDto taskDto) {
        Task task = taskService.updateById(id, taskMapper.mapToEntity(taskDto));
        return ResponseEntity.ok(taskMapper.mapToDto(task));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable("id") UUID id) {
        Task task = taskService.getById(id);
        return ResponseEntity.ok(taskMapper.mapToDto(task));
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAll() {
        List<Task> tasks = taskService.getAll();
        return ResponseEntity.ok(tasks.stream().map(taskMapper::mapToDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
