package com.amss.homemanagement.mapper;

import com.amss.homemanagement.dto.FamilyMemberDto;
import com.amss.homemanagement.model.FamilyMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FamilyMemberMapper {

    @Mapping(source = "member.id", target = "memberId")
    @Mapping(source = "family.id", target = "familyId")
    FamilyMemberDto mapToDto(FamilyMember familyMember);
}
