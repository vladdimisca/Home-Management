package com.amss.homemanagement.controller;

import com.amss.homemanagement.dto.FamilyDto;
import com.amss.homemanagement.dto.RequestDto;
import com.amss.homemanagement.mapper.RequestMapper;
import com.amss.homemanagement.model.Family;
import com.amss.homemanagement.model.Request;
import com.amss.homemanagement.model.RequestStatus;
import com.amss.homemanagement.service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @PostMapping
    public ResponseEntity<RequestDto> create(@Valid @RequestBody RequestDto requestDto) {
        Request request = requestService.create(requestMapper.mapToEntity(requestDto), requestDto.familyId());
        return ResponseEntity
                .created(URI.create("/api/requests/" + request.getId()))
                .body(requestMapper.mapToDto(request));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<RequestDto> accept(@PathVariable("id") UUID id) {
        Request request = requestService.updateStatusById(id, RequestStatus.ACCEPTED);
        return ResponseEntity.ok(requestMapper.mapToDto(request));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<RequestDto> reject(@PathVariable("id") UUID id) {
        Request request = requestService.updateStatusById(id, RequestStatus.REJECTED);
        return ResponseEntity.ok(requestMapper.mapToDto(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDto> getById(@PathVariable("id") UUID id) {
        Request request = requestService.getById(id);
        return ResponseEntity.ok(requestMapper.mapToDto(request));
    }

    @GetMapping
    public ResponseEntity<List<RequestDto>> getAll(@RequestParam(value = "familyId") UUID familyId,
                                                   @RequestParam(value = "status", required = false) RequestStatus status) {
        List<Request> requests = requestService.getAllByFamilyIdAndStatus(familyId, status);
        return ResponseEntity.ok(requests.stream().map(requestMapper::mapToDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        requestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
