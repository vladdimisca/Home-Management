package com.amss.homemanagement.controller;

import com.amss.homemanagement.dto.UserDto;
import com.amss.homemanagement.mapper.UserMapper;
import com.amss.homemanagement.model.User;
import com.amss.homemanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping
    @PermitAll
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userDto) {
        User user = userService.create(userMapper.mapToEntity(userDto));
        return ResponseEntity
                .created(URI.create("/api/users/" + user.getId()))
                .body(userMapper.mapToDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable("id") UUID id, @Valid @RequestBody UserDto userDto) {
        User user = userService.update(id, userMapper.mapToEntity(userDto));
        return ResponseEntity.ok(userMapper.mapToDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(userMapper.mapToDto(userService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users.stream().map(userMapper::mapToDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") UUID id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
