package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.model.*;
import com.amss.homemanagement.repository.FamilyRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserService userService;
    private final SecurityService securityService;
    private final EntityManager em;

    public Family create(Family family) {
        User user = userService.getById(securityService.getUserId());
        FamilyMember familyMember = createFamilyMember(user, family, true);
        family.getFamilyMembers().add(familyMember);

        return familyRepository.save(family);
    }

    public Family getById(UUID id) {
        return familyRepository.findById(id).orElseThrow(() ->
                new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "family", id));
    }

    public Family updateById(UUID id, Family family) {
        User user = userService.getById(securityService.getUserId());
        Family existingFamily = getById(id);
        checkUserIsFamilyMemberWithAdminRights(user, existingFamily);

        existingFamily.setName(family.getName());
        return familyRepository.save(existingFamily);
    }

    public List<Family> getAll(boolean asMember) {
        return asMember ? familyRepository.findAllByUserId(securityService.getUserId()) : familyRepository.findAll();
    }

    public List<Task> getTasksByFamilyId(UUID familyId) {
        User user = userService.getById(securityService.getUserId());
        Family family = getById(familyId);
        if (getFamilyMember(user, family).isEmpty()) {
            throw new ExceptionFactory().createException(HttpStatus.FORBIDDEN, ErrorMessage.NOT_PART_OF_FAMILY);
        }
        return family.getTasks();
    }

    @Transactional
    public void deleteById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Family family = getById(id);
        checkUserIsFamilyMemberWithAdminRights(user, family);

        familyRepository.deleteFamilyMembersByFamilyId(family.getId());
        family.getFamilyMembers().clear();

        familyRepository.delete(family);
    }

    public void addMember(User user, Family family) {
        if (getFamilyMember(user, family).isPresent()) {
            throw new ExceptionFactory().createException(HttpStatus.CONFLICT, ErrorMessage.ALREADY_EXISTS, "Member");
        }
        FamilyMember familyMember = createFamilyMember(user, family, false);
        family.getFamilyMembers().add(familyMember);
        familyRepository.save(family);
    }

    public void deleteMember(UUID userId, UUID familyId) {
        User userToBeRemoved = userService.getById(userId);
        User loggedUser = userService.getById(securityService.getUserId());
        Family family = getById(familyId);
        checkUserIsFamilyMemberWithAdminRights(loggedUser, family);
        if (getFamilyMember(userToBeRemoved, family).isEmpty()) {
            throw new ExceptionFactory().createException(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND, "member", userToBeRemoved.getId());
        }

        family.getFamilyMembers().remove(getFamilyMember(userToBeRemoved, family).get());
        familyRepository.save(family);
    }

    private FamilyMember createFamilyMember(User user, Family family, boolean isAdmin) {
        FamilyMember familyMember = new FamilyMember();
        familyMember.setIsAdmin(isAdmin);
        familyMember.setMember(user);
        familyMember.setFamily(family);
        return familyMember;
    }

    private void checkUserIsFamilyMemberWithAdminRights(User user, Family family) {
        // A user which is not family member with admin rights is not allowed to update/delete the family
        Optional<FamilyMember> familyMember = getFamilyMember(user, family);
        if (familyMember.isEmpty() || Boolean.FALSE.equals(familyMember.get().getIsAdmin())) {
            throw new ExceptionFactory().createException(HttpStatus.FORBIDDEN, ErrorMessage.MUST_BE_ADMIN_FAMILY_MEMBER);
        }
    }

    public Optional<FamilyMember> getFamilyMember(User user, Family family) {
        return user.getFamilyMembers().stream()
                .filter(familyMember -> familyMember.getFamily().equals(family))
                .findFirst();
    }

    public List<FamilyMember> getFamilyMembersByFamilyId(UUID familyId) {
        User user = userService.getById(securityService.getUserId());
        Family family = getById(familyId);
        if (getFamilyMember(user, family).isEmpty()) {
            throw new ExceptionFactory().createException(HttpStatus.FORBIDDEN, ErrorMessage.NOT_PART_OF_FAMILY);
        }
        return family.getFamilyMembers();
    }

    public FamilyMember updateFamilyMemberRole(UUID familyId, UUID userId, Role role) {
        List<FamilyMember> familyMembers = getFamilyMembersByFamilyId(familyId);
        FamilyMember existingMember = familyMembers.stream()
                .filter(member -> member.getMember().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ExceptionFactory().createException(HttpStatus.NOT_FOUND,
            ErrorMessage.NOT_FOUND, "member", userId));

        existingMember.setRole(role);

        familyRepository.save(existingMember.getFamily());
        return existingMember;
    }
}
