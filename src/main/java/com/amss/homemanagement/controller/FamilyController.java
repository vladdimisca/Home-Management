package com.amss.homemanagement.controller;

import com.amss.homemanagement.dto.FamilyDto;
import com.amss.homemanagement.dto.FamilyMemberDto;
import com.amss.homemanagement.dto.TaskDto;
import com.amss.homemanagement.mapper.FamilyMapper;
import com.amss.homemanagement.mapper.FamilyMemberMapper;
import com.amss.homemanagement.mapper.TaskMapper;
import com.amss.homemanagement.model.Family;
import com.amss.homemanagement.model.FamilyMember;
import com.amss.homemanagement.model.Role;
import com.amss.homemanagement.model.Task;
import com.amss.homemanagement.service.FamilyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/families")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;
    private final FamilyMapper familyMapper;
    private final TaskMapper taskMapper;
    private final FamilyMemberMapper familyMemberMapper;

    @PostMapping
    public ResponseEntity<FamilyDto> create(@Valid @RequestBody FamilyDto familyDto) {
        Family family = familyService.create(familyMapper.mapToEntity(familyDto));
        return ResponseEntity
                .created(URI.create("/api/families/" + family.getId()))
                .body(familyMapper.mapToDto(family));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FamilyDto> update(@PathVariable("id") UUID id, @Valid @RequestBody FamilyDto familyDto) {
        Family family = familyService.updateById(id, familyMapper.mapToEntity(familyDto));
        return ResponseEntity.ok(familyMapper.mapToDto(family));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FamilyDto> getById(@PathVariable("id") UUID id) {
        Family family = familyService.getById(id);
        return ResponseEntity.ok(familyMapper.mapToDto(family));
    }

    @GetMapping
    public ResponseEntity<List<FamilyDto>> getAll(@RequestParam(value = "asMember", required = false) boolean asMember) {
        List<Family> families = familyService.getAll(asMember);
        return ResponseEntity.ok(families.stream().map(familyMapper::mapToDto).toList());
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDto>> getTasksByFamilyId(@PathVariable("id") UUID familyId) {
        List<Task> tasks = familyService.getTasksByFamilyId(familyId);
        return ResponseEntity.ok(tasks.stream().map(taskMapper::mapToDto).toList());
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<FamilyMemberDto>> getFamilyMembersByFamilyId(@PathVariable("id") UUID familyId) {
        List<FamilyMember> familyMembers = familyService.getFamilyMembersByFamilyId(familyId);
        return ResponseEntity.ok(familyMembers.stream().map(familyMemberMapper::mapToDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        familyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{familyId}/members/{userId}")
    public ResponseEntity<?> delete(@PathVariable("familyId") UUID familyId, @PathVariable("userId") UUID userId) {
        familyService.deleteMember(userId, familyId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{familyId}/members/{userId}")
    public ResponseEntity<FamilyMemberDto> updateFamilyMemberRole(@PathVariable("familyId") UUID familyId,
                                                              @PathVariable("userId") UUID userId,
                                                              @Valid @RequestBody FamilyMemberDto familyMemberDto) {
        FamilyMember familyMember = familyService.updateFamilyMemberRole(familyId, userId, familyMemberDto.role());
        return ResponseEntity.ok(familyMemberMapper.mapToDto(familyMember));
    }
}
