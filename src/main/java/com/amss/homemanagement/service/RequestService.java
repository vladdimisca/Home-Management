package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.model.*;
import com.amss.homemanagement.repository.RequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final FamilyService familyService;
    private final SecurityService securityService;

    public Request create(Request request, UUID familyId) {
        User user = userService.getById(securityService.getUserId());
        Family family = familyService.getById(familyId);

        if (familyService.getFamilyMember(user, family).isPresent()) {
            throw new ExceptionFactory().createException(HttpStatus.CONFLICT, ErrorMessage.ALREADY_PART_OF_FAMILY, family.getId());
        }
        if (hasAnyPendingRequest(user, family)) {
            throw new ExceptionFactory().createException(HttpStatus.CONFLICT, ErrorMessage.ALREADY_EXISTS, "A pending request");
        }

        request.setUser(user);
        request.setFamily(family);
        request.setStatus(RequestStatus.PENDING);
        return requestRepository.save(request);
    }

    public Request getById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Request request = requestRepository.findById(id).orElseThrow(() ->
                new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "request", id));

        Optional<FamilyMember> member = familyService.getFamilyMember(user, request.getFamily());
        if (!request.getUser().equals(user) && (member.isEmpty() || !member.get().getIsAdmin())) {
            throw new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "request", id);
        }
        return request;
    }

    @Transactional
    public Request updateStatusById(UUID id, RequestStatus requestStatus) {
        User user = userService.getById(securityService.getUserId());
        Request existingRequest = getById(id);
        Optional<FamilyMember> member = familyService.getFamilyMember(user, existingRequest.getFamily());

        if (member.isEmpty() || !member.get().getIsAdmin()) {
            throw new ExceptionFactory().createException(HttpStatus.FORBIDDEN, ErrorMessage.MUST_BE_ADMIN_FAMILY_MEMBER);
        }
        if (existingRequest.getStatus() != RequestStatus.PENDING) {
            throw new ExceptionFactory().createException(HttpStatus.FORBIDDEN, ErrorMessage.REQUEST_NOT_PENDING);
        }
        if (requestStatus == RequestStatus.ACCEPTED) {
            familyService.addMember(existingRequest.getUser(), existingRequest.getFamily());
        }

        existingRequest.setStatus(requestStatus);
        return requestRepository.save(existingRequest);
    }

    public List<Request> getAllByFamilyIdAndStatus(UUID familyId, RequestStatus status) {
        User user = userService.getById(securityService.getUserId());
        Family family = familyService.getById(familyId);
        Optional<FamilyMember> member = familyService.getFamilyMember(user, family);
        if (member.isEmpty()) {
            throw new ExceptionFactory().createException(HttpStatus.FORBIDDEN, ErrorMessage.NOT_PART_OF_FAMILY);
        }

        List<Request> requests = requestRepository.findByFamily_IdAndStatus(familyId, status);
        if (member.get().getIsAdmin()) {
            return requests;
        }
        return requests.stream().filter(req -> req.getUser().equals(user)).toList();
    }

    public void deleteById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Request request = getById(id);
        if (!request.getUser().equals(user)) {
            throw new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "request", id);
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new ExceptionFactory().createException(HttpStatus.FORBIDDEN, ErrorMessage.REQUEST_NOT_PENDING);
        }

        requestRepository.delete(getById(id));
    }

    private boolean hasAnyPendingRequest(User user, Family family) {
        return user.getRequests().stream()
                .filter(req -> req.getFamily().getId().equals(family.getId()))
                .anyMatch(req -> req.getStatus() == RequestStatus.PENDING);
    }
}
