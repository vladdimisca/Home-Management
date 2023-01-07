package com.amss.homemanagement.mapper;

import com.amss.homemanagement.dto.FamilyDto;
import com.amss.homemanagement.model.Family;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FamilyMapper {

    FamilyDto mapToDto(Family family);

    Family mapToEntity(FamilyDto familyDto);
}
