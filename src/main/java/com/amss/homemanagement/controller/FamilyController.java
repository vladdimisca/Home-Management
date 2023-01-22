package com.amss.homemanagement.controller;

import com.amss.homemanagement.dto.FamilyDto;
import com.amss.homemanagement.mapper.FamilyMapper;
import com.amss.homemanagement.model.Family;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        familyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
